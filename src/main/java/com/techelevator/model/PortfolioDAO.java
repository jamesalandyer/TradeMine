package com.techelevator.model;

import java.util.List;

public interface PortfolioDAO {

	public void savePortfolio(Portfolio portfolio);
	
	public List<Portfolio> getPortfoliosForGame(Long gameId, Long userId);
	
}
