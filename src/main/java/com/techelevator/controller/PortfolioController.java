package com.techelevator.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.model.Player;
import com.techelevator.model.PlayerDAO;
import com.techelevator.model.PortfolioDAO;
import com.techelevator.model.Sale;
import com.techelevator.model.SaleDAO;
import com.techelevator.model.User;

@Controller
public class PortfolioController {
	
	private PortfolioDAO portfolioDAO;
	private PlayerDAO playerDAO;
	private SaleDAO saleDAO;
	
	@Autowired
	public PortfolioController(PortfolioDAO portfolioDAO, PlayerDAO playerDAO, SaleDAO saleDAO) {
		this.portfolioDAO = portfolioDAO;
		this.playerDAO = playerDAO;
		this.saleDAO = saleDAO;
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
		request.setAttribute("playerInvites", playerDAO.getInvitesForUser(userId));
		request.setAttribute("gamePlayer", player);
		request.setAttribute("playerPortfolios", portfolioDAO.getPortfoliosForGame(gameId, userId));
		request.setAttribute("apiKey", "6NAAJT5VBIBTUPFE");
		
		return "portfolio";
	}
	
}
