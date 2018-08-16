package com.techelevator.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String displayHome(HttpSession session) {
		if(session.getAttribute("currentUser") == null) {
			return "redirect:/login";
		}
		return "home";
	}
	
	@RequestMapping(path="/game", method=RequestMethod.GET)
	public String displayGame(HttpSession session) {
		if(session.getAttribute("currentUser") == null) {
			return "redirect:/login";
		}
		return "game";
	}
	
}
