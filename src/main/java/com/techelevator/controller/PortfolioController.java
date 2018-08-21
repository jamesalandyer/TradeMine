package com.techelevator.controller;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
		
		return "portfolio";
	}
	
	@RequestMapping(path="/game/{gameId}/purchase", method=RequestMethod.POST)
	public String purchaseStock(HttpSession session, @PathVariable Long gameId, @RequestParam Long shares, @RequestParam String stockTicker, @RequestParam Double price, @RequestParam Double amountLeft) {
		User user = (User) session.getAttribute("currentUser");
		if(user == null) {
			return "redirect:/login";
		}
		
		Sale newSale = new Sale();
		newSale.setGameId(gameId);
		newSale.setPricePerShare(price);
		newSale.setPurchase(true);
		newSale.setShares(shares);
		newSale.setStockSymbol(stockTicker);
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
	
}
