package com.techelevator.controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.techelevator.model.Game;
import com.techelevator.model.GameDAO;
import com.techelevator.model.Player;
import com.techelevator.model.PlayerDAO;
import com.techelevator.model.Portfolio;
import com.techelevator.model.PortfolioDAO;
import com.techelevator.model.Sale;
import com.techelevator.model.SaleDAO;
import com.techelevator.model.User;
import com.techelevator.model.UserDAO;

@Controller
public class HomeController {
	
	private GameDAO gameDAO;
	private PlayerDAO playerDAO;
	private UserDAO userDAO;
	private PortfolioDAO portfolioDAO;
	private SaleDAO saleDAO;

	@Autowired
	public HomeController(GameDAO gameDAO, PlayerDAO playerDAO, UserDAO userDAO, PortfolioDAO portfolioDAO, SaleDAO saleDAO) {
		this.gameDAO = gameDAO;
		this.playerDAO = playerDAO;
		this.userDAO = userDAO;
		this.portfolioDAO = portfolioDAO;
		this.saleDAO = saleDAO;
	}
	
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String displayHome(HttpServletRequest request, HttpSession session) {
		Object user = session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		Long userId = ((User) user).getUserId();
		request.setAttribute("playerInvites", playerDAO.getInvitesForUser(userId));
		request.setAttribute("gamesList", gameDAO.getGamesWithUser(userId));
		request.setAttribute("gameBalances", playerDAO.getPlayerBalanceForGames(userId));
		
		return "home";
	}
	
	@RequestMapping(path="/game/{gameId}", method=RequestMethod.GET)
	public String displayGame(HttpServletRequest request, HttpSession session, @PathVariable Long gameId) {
		Object user = session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		
		Long userId = ((User) user).getUserId();
		if(!playerDAO.getPlayerForGame(userId, gameId).isJoined()) {
			return "redirect:/";
		}
		
		request.setAttribute("playerInvites", playerDAO.getInvitesForUser(userId));
		Game currentGame = gameDAO.getGame(gameId);
		request.setAttribute("game", currentGame);
		List<Player> allPlayers = playerDAO.getPlayersForGame(gameId);
		List<Player> gamePlayers = allPlayers.stream().filter(player -> player.isJoined()).collect(Collectors.toList());
		request.setAttribute("players", gamePlayers);
		BigDecimal total = gamePlayers.stream().map(player -> player.getAmountLeft()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		request.setAttribute("total", formatter.format(total));
		request.setAttribute("users", userDAO.getAllUsers().stream()
			.filter(u -> allPlayers.stream()
					.map(player -> player.getUserId()).collect(Collectors.toList()).contains(u.getUserId()) == false).collect(Collectors.toList()));
		Map<Long, List<Portfolio>> playerPortfolios = new HashMap<>();
		for (Player player: gamePlayers) {
			playerPortfolios.put(player.getUserId(), portfolioDAO.getPortfoliosForGame(gameId, player.getUserId()));
		}
		request.setAttribute("playerPortfolios", playerPortfolios);
		request.setAttribute("soldAll", currentGame.isEnded());
		if (!currentGame.isEnded()) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
			try {
				Date gameEndDate = format.parse(currentGame.getEndDate());
				request.setAttribute("gameEnded", (gameEndDate.before(new Date())));
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
				request.setAttribute("gameEndedDate", formatDate.format(gameEndDate));
			} catch (ParseException e) {
				request.setAttribute("gameEnded", false);
				request.setAttribute("gameEndedDate", "invalid");
				e.printStackTrace();
			}
		} else {
			request.setAttribute("gameEnded", true);
			request.setAttribute("gameEndedDate", "invalid");
		}
		
		return "game";
	}
	
	@RequestMapping(path="/game/{gameId}/ended", method=RequestMethod.POST)
	public String displayGameEnded(HttpServletRequest request, HttpSession session, @PathVariable Long gameId, String sellData) {
		Object user = session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		
		Long userId = ((User) user).getUserId();
		if(!playerDAO.getPlayerForGame(userId, gameId).isJoined()) {
			return "redirect:/";
		}
		
		Game currentGame = gameDAO.getGame(gameId);
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
		Date today = new Date();
		Date gameEndDate = today;
		Date endDateFormatted = today;
		try {
			gameEndDate = format.parse(currentGame.getEndDate());
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
			endDateFormatted = formatDate.parse(formatDate.format(gameEndDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (currentGame.isEnded() || !gameEndDate.before(today)) {
			return "redirect:/game/" + gameId;
		}
		
		playerDAO.removeAllUnjoined(gameId);
		
		Map<String, Double> stocks = new HashMap<>();
		String[] stocksData = sellData.split("/");
		for (int i = 0; i < stocksData.length; i++) {
			String[] stockData = stocksData[i].split(":");
			stocks.put(stockData[0], Double.parseDouble(stockData[1]));
		}
		
		List<Player> allPlayers = playerDAO.getPlayersForGame(gameId);
		List<Player> gamePlayers = allPlayers.stream().filter(player -> player.isJoined()).collect(Collectors.toList());
		for (Player player: gamePlayers) {
			List<Portfolio> portfolios = portfolioDAO.getPortfoliosForGame(gameId, player.getUserId());
			BigDecimal totalSales = BigDecimal.ZERO;
			for (Portfolio portfolio: portfolios) {
				Sale newSale = new Sale();
				newSale.setGameId(gameId);
				newSale.setPurchase(false);
				newSale.setShares(portfolio.getShares());
				newSale.setStockSymbol(portfolio.getStockSymbol());
				newSale.setUserId(portfolio.getUserId());
				newSale.setTransactionDate(endDateFormatted);
				Double pricePerShare = stocks.get(portfolio.getStockSymbol());
				newSale.setPricePerShare(pricePerShare);
				totalSales = totalSales.add(new BigDecimal(pricePerShare).multiply(new BigDecimal(portfolio.getShares())));
				saleDAO.saveSale(newSale);
				portfolio.setShares(0L);
				portfolioDAO.updatePortfolio(portfolio);
			}
			player.setAmountLeft(player.getAmountLeft().add(totalSales));
			playerDAO.updatePlayer(player);
		}
		
		currentGame.setEnded(true);
		gameDAO.updateGame(currentGame);
		
		return "redirect:/game/" + gameId;
	}
	
	@RequestMapping(path="/game/create", method=RequestMethod.POST)
	public String createGame(HttpSession session, @RequestParam String endDate, @RequestParam String gameName) {
		if(session.getAttribute("currentUser") == null) {
			return "redirect:/login";
		}
		
		Game newGame = new Game();
		
		newGame.setCreatorId(((User) session.getAttribute("currentUser")).getUserId());
		newGame.setEndDate(endDate);
		newGame.setGameName(gameName);
		
		gameDAO.saveGame(newGame);
		
		return "redirect:/";
	}
	
}
