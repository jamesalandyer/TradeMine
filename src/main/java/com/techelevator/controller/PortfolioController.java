package com.techelevator.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

@Controller
public class PortfolioController {
	
	private PortfolioDAO portfolioDAO;
	private GameDAO gameDAO;
	private PlayerDAO playerDAO;
	private SaleDAO saleDAO;
	
	@Autowired
	public PortfolioController(PortfolioDAO portfolioDAO, PlayerDAO playerDAO, SaleDAO saleDAO, GameDAO gameDAO) {
		this.portfolioDAO = portfolioDAO;
		this.playerDAO = playerDAO;
		this.saleDAO = saleDAO;
		this.gameDAO = gameDAO;
	}
	
	@RequestMapping(path="/game/{gameId}/{userName}", method=RequestMethod.GET)
	public String displayPortfolio(HttpServletRequest request, HttpSession session, @PathVariable Long gameId, @PathVariable String userName) {
		User user = (User) session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		if(!(user.getUserName().equals(userName))) {
			return "redirect:/game/" + gameId;
		}
		Long userId = user.getUserId();
		Player player = playerDAO.getPlayerForGame(userId, gameId);
		if(!player.isJoined()) {
			return "redirect:/";
		}
		Game game = gameDAO.getGame(gameId);
		if (!game.isEnded()) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
			try {
				Date gameEndDate = format.parse(game.getEndDate());
				if (gameEndDate.before(new Date())) {
					return "redirect:/game/" + gameId;
				} else {
					request.setAttribute("gameEnded", false);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			request.setAttribute("gameEnded", true);
		}
		request.setAttribute("playerInvites", playerDAO.getInvitesForUser(userId));
		request.setAttribute("gamePlayer", player);
		request.setAttribute("playerPortfolios", portfolioDAO.getPortfoliosForGame(gameId, userId));
		
		return "portfolio";
	}
	
	@RequestMapping(path="/game/{gameId}/{userName}/trades", method=RequestMethod.GET)
	public String displayTrades(HttpServletRequest request, HttpSession session, @PathVariable Long gameId, @PathVariable String userName) {
		User user = (User) session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		if(!(user.getUserName().equals(userName))) {
			return "redirect:/game/" + gameId;
		}
		Long userId = user.getUserId();
		Player player = playerDAO.getPlayerForGame(userId, gameId);
		if(!player.isJoined()) {
			return "redirect:/";
		}
		Game game = gameDAO.getGame(gameId);
		if (!game.isEnded()) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
			try {
				Date gameEndDate = format.parse(game.getEndDate());
				if (gameEndDate.before(new Date())) {
					return "redirect:/game/" + gameId;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("playerInvites", playerDAO.getInvitesForUser(userId));
		request.setAttribute("gameName", player.getGameName());
		request.setAttribute("trades", saleDAO.getSalesByGameAndPlayer(gameId, userId));
		
		return "trades";
	}
	
	@RequestMapping(path="/game/{gameId}/purchase", method=RequestMethod.POST)
	public String purchaseStock(HttpSession session, @PathVariable Long gameId, @RequestParam Long shares, @RequestParam String stockTicker, @RequestParam Double price, @RequestParam Double amountLeft) {
		User user = (User) session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		
		Game game = gameDAO.getGame(gameId);
		if (!game.isEnded()) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
			try {
				Date gameEndDate = format.parse(game.getEndDate());
				if (gameEndDate.before(new Date())) {
					return "redirect:/";
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			return "redirect:/";
		}
		
		Sale newSale = new Sale();
		newSale.setGameId(gameId);
		newSale.setPricePerShare(price);
		newSale.setPurchase(true);
		newSale.setShares(shares);
		newSale.setStockSymbol(stockTicker.toUpperCase());
		newSale.setTransactionDate(new Date());
		newSale.setUserId(user.getUserId());
		saleDAO.saveSale(newSale);
		Player updatePlayer = new Player();
		updatePlayer.setAmountLeft(new BigDecimal(amountLeft).subtract((new BigDecimal(shares).multiply(new BigDecimal(price)))));
		updatePlayer.setGameId(gameId);
		updatePlayer.setUserId(user.getUserId());
		playerDAO.updatePlayer(updatePlayer);
		
		Portfolio currentPortfolio = portfolioDAO.getPortfolio(gameId, user.getUserId(), stockTicker);
		if (currentPortfolio != null) {
			currentPortfolio.setShares(currentPortfolio.getShares() + shares);
			portfolioDAO.updatePortfolio(currentPortfolio);
		} else {
			Portfolio newPortfolio = new Portfolio();
			newPortfolio.setGameId(gameId);
			newPortfolio.setShares(shares);
			newPortfolio.setStockSymbol(stockTicker);
			newPortfolio.setUserId(user.getUserId());
			portfolioDAO.savePortfolio(newPortfolio);
		}
		
		
		return "redirect:/game/" + gameId + "/" + user.getUserName();
	}
	
	@RequestMapping(path="/game/{gameId}/sell", method=RequestMethod.POST)
	public String sellStock(HttpSession session, @PathVariable Long gameId, @RequestParam Long shares, @RequestParam String stockTicker, @RequestParam Double price, @RequestParam Double amountLeft) {
		User user = (User) session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		
		Game game = gameDAO.getGame(gameId);
		if (!game.isEnded()) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
			try {
				Date gameEndDate = format.parse(game.getEndDate());
				if (gameEndDate.before(new Date())) {
					return "redirect:/";
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			return "redirect:/";
		}
		
		Sale newSale = new Sale();
		newSale.setGameId(gameId);
		newSale.setPricePerShare(price);
		newSale.setPurchase(false);
		newSale.setShares(shares);
		newSale.setStockSymbol(stockTicker.toUpperCase());
		newSale.setTransactionDate(new Date());
		newSale.setUserId(user.getUserId());
		saleDAO.saveSale(newSale);
		Player updatePlayer = new Player();
		updatePlayer.setAmountLeft(new BigDecimal(amountLeft).add((new BigDecimal(shares).multiply(new BigDecimal(price)))));
		updatePlayer.setGameId(gameId);
		updatePlayer.setUserId(user.getUserId());
		playerDAO.updatePlayer(updatePlayer);
		
		Portfolio currentPortfolio = portfolioDAO.getPortfolio(gameId, user.getUserId(), stockTicker);
		if (currentPortfolio.getShares() == shares) {
			portfolioDAO.removePortfolio(currentPortfolio);
		} else {
			currentPortfolio.setShares(currentPortfolio.getShares() - shares);
			portfolioDAO.updatePortfolio(currentPortfolio);
		}
		
		
		return "redirect:/game/" + gameId + "/" + user.getUserName();
	}
	
}
