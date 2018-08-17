package com.techelevator.model;

import java.util.List;

public interface GameDAO {

	public void saveGame(Game game);
	
	public Game getGame(Long gameId);
	
	public List<Game> getGamesWithUser(Long userId);
	
}
