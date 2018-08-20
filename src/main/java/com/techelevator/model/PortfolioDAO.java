package com.techelevator.model;

import java.util.List;

public interface PortfolioDAO {

	public void savePortfolio(Portfolio portfolio);
	
	public void updatePortfolio(Portfolio portfolio);
	
	public void removePortfolio(Portfolio portfolio);
	
	public List<Portfolio> getPortfoliosForGame(Long gameId, Long userId);
	
}
