package com.techelevator.controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.techelevator.model.Game;
import com.techelevator.model.GameDAO;
import com.techelevator.model.Player;
import com.techelevator.model.PlayerDAO;
import com.techelevator.model.User;
import com.techelevator.model.UserDAO;

@Controller
public class HomeController {
	
	private GameDAO gameDAO;
	private PlayerDAO playerDAO;
	private UserDAO userDAO;

	@Autowired
	public HomeController(GameDAO gameDAO, PlayerDAO playerDAO, UserDAO userDAO) {
		this.gameDAO = gameDAO;
		this.playerDAO = playerDAO;
		this.userDAO = userDAO;
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
		request.setAttribute("playerInvites", playerDAO.getInvitesForUser(userId));
		request.setAttribute("game", gameDAO.getGame(gameId));
		List<Player> allPlayers = playerDAO.getPlayersForGame(gameId);
		List<Player> gamePlayers = allPlayers.stream().filter(player -> player.isJoined()).collect(Collectors.toList());
		request.setAttribute("players", gamePlayers);
		BigDecimal total = gamePlayers.stream().map(player -> player.getAmountLeft()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		request.setAttribute("total", formatter.format(total));
		request.setAttribute("users", userDAO.getAllUsers().stream()
			.filter(u -> allPlayers.stream()
					.map(player -> player.getUserId()).collect(Collectors.toList()).contains(u.getUserId()) == false).collect(Collectors.toList()));
		
		return "game";
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
