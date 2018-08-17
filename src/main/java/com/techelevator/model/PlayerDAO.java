package com.techelevator.model;

import java.util.List;

public interface PlayerDAO {

	public void savePlayer(Player player);
	
	public List<Player> getPlayersForGame(Long gameId);
	
	public List<Player> getInvitesForUser(Long userId);
	
}
