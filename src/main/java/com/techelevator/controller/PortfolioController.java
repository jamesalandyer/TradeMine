package com.techelevator.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.model.PlayerDAO;
import com.techelevator.model.PortfolioDAO;
import com.techelevator.model.User;

@Controller
public class PortfolioController {
	
	private PortfolioDAO portfolioDAO;
	private PlayerDAO playerDAO;
	
	@Autowired
	public PortfolioController(PortfolioDAO portfolioDAO, PlayerDAO playerDAO) {
		this.portfolioDAO = portfolioDAO;
		this.playerDAO = playerDAO;
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
		request.setAttribute("playerInvites", playerDAO.getInvitesForUser(userId));
		
		return "portfolio";
	}
	
}
