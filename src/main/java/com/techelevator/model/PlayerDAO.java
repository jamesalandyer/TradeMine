package com.techelevator.model;

import java.util.List;

public interface PlayerDAO {

	public void savePlayer(Player player);
	
	public void updatePlayer(Player player);
	
	public void acceptInvite(Player invite);
	
	public void declineInvite(Player invite);
	
	public List<Player> getPlayersForGame(Long gameId);
	
	public List<Player> getInvitesForUser(Long userId);
	
	public Player getPlayerForGame(Long userId, Long gameId);
	
	public List<String> getPlayerBalanceForGames(Long userId);

	public void removeAllUnjoined(Long gameId);
	
}
