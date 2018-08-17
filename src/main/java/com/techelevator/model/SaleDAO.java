package com.techelevator.model;

import java.util.List;

public interface SaleDAO {

	public void saveSale(Sale sale);
	
	public List<Sale> getSalesByGame(Long gameId);
	
	public List<Sale> getSalesByGameAndPlayer(Long gameId, Long userId);
	
}
