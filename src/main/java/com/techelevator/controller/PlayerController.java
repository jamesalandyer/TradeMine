package com.techelevator.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.techelevator.model.Game;
import com.techelevator.model.GameDAO;
import com.techelevator.model.Player;
import com.techelevator.model.PlayerDAO;
import com.techelevator.model.User;

@Controller
public class PlayerController {

	private PlayerDAO playerDAO;
	private GameDAO gameDAO;
	
	@Autowired
	public PlayerController(PlayerDAO playerDAO, GameDAO gameDAO) {
		this.playerDAO = playerDAO;
		this.gameDAO = gameDAO;
	}
	
	@RequestMapping(path="/invites", method=RequestMethod.GET)
	public String displayInvites(HttpServletRequest request, HttpSession session) {
		Object user = session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		Long userId = ((User) user).getUserId();
		request.setAttribute("playerInvites", playerDAO.getInvitesForUser(userId));
		
		return "invites";
	}
	
	@RequestMapping(path="/invites/send", method=RequestMethod.POST)
	public String sendInvite(HttpSession session, @RequestParam Long inviteeId, @RequestParam Long gameId) {
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
					return "redirect:/game/" + gameId;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			return "redirect:/game/" + gameId;
		}
		
		Player newPlayer = new Player();
		
		newPlayer.setAmountLeft(new BigDecimal(100000));
		newPlayer.setGameId(gameId);
		newPlayer.setInviterId(user.getUserId());
		newPlayer.setJoined(false);
		newPlayer.setUserId(inviteeId);
		
		playerDAO.savePlayer(newPlayer);
		
		return "redirect:/game/" + gameId;
	}
	
	@RequestMapping(path="/invites/confirm", method=RequestMethod.POST)
	public String confirmInvite(HttpSession session, @RequestParam String accept, @RequestParam Long gameId) {
		User user = (User) session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		
		Player player = new Player();
		
		player.setGameId(gameId);
		player.setUserId(user.getUserId());
		
		Game game = gameDAO.getGame(gameId);
		if (!game.isEnded()) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
			try {
				Date gameEndDate = format.parse(game.getEndDate());
				if (gameEndDate.before(new Date())) {
					playerDAO.declineInvite(player);
					return "redirect:/invites";
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			playerDAO.declineInvite(player);
			return "redirect:/invites";
		}
		
		if (accept.equals("true")) {
			playerDAO.acceptInvite(player);
		} else {
			playerDAO.declineInvite(player);
		}
		
		return "redirect:/invites";
	}
	
}
