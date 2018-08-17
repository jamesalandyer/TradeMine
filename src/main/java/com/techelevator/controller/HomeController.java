package com.techelevator.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.techelevator.model.Game;
import com.techelevator.model.GameDAO;
import com.techelevator.model.User;

@Controller
public class HomeController {
	
	private GameDAO gameDAO;

	@Autowired
	public HomeController(GameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}
	
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String displayHome(HttpServletRequest request, HttpSession session) {
		if(session.getAttribute("currentUser") == null) {
			return "redirect:/login";
		}
		
		request.setAttribute("gamesList", gameDAO.getGamesWithUser(((User) session.getAttribute("currentUser")).getUserId()));
		
		return "home";
	}
	
	@RequestMapping(path="/game", method=RequestMethod.GET)
	public String displayGame(HttpSession session) {
		if(session.getAttribute("currentUser") == null) {
			return "redirect:/login";
		}
		
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
