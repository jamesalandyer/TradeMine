package com.techelevator.model;

import java.math.BigDecimal;
import java.util.List;

public interface PlayerDAO {

	public void savePlayer(Player player);
	
	public void acceptInvite(Player invite);
	
	public void declineInvite(Player invite);
	
	public List<Player> getPlayersForGame(Long gameId);
	
	public List<Player> getInvitesForUser(Long userId);
	
	public BigDecimal getPlayerBalanceForGame(Long userId, Long gameId);
	
	public List<String> getPlayerBalanceForGames(Long userId);
	
}
