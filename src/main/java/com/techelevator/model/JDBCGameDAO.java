package com.techelevator.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JDBCGameDAO implements GameDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCGameDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void saveGame(Game game) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String insertStatement = "INSERT INTO game(game_name, creator_id, end_date, ended) VALUES (?, ?, ?, false)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, game.getGameName());
				ps.setLong(2, game.getCreatorId());
				ps.setString(3, game.getEndDate());
				return ps;
			}
		}, keyHolder);
		jdbcTemplate.update("INSERT INTO player VALUES (?, ?, ?, ?, ?)", new BigDecimal(100000), game.getCreatorId(), true, game.getCreatorId(), keyHolder.getKeys().get("game_id"));
	}

	@Override
	public void updateGame(Game game) {
		String sqlUpdateGame = "UPDATE game SET ended = ? WHERE game_id = ?";
		jdbcTemplate.update(sqlUpdateGame, game.isEnded(), game.getGameId());
	}
	
	@Override
	public Game getGame(Long gameId) {
		String sqlGetGame = "SELECT * " + "FROM game " + "WHERE game_id = ? ";

		SqlRowSet game = jdbcTemplate.queryForRowSet(sqlGetGame, gameId);
		if (game.next()) {
			return mapRowToGame(game, false);
		}
		return null;
	}

	@Override
	public List<Game> getGamesWithUser(Long userId) {
		String sqlGetGames = "SELECT g.game_id, g.game_name, g.creator_id, g.end_date, g.ended, count(p.user_id) AS player_count "
				+ "FROM game g "
				+ "INNER JOIN player p ON g.game_id = p.game_id "
				+ "WHERE g.game_id IN (select pl.game_id FROM player pl WHERE pl.user_id = ? AND pl.joined IS TRUE) "
				+ "AND p.joined IS TRUE "
				+ "GROUP BY g.game_id "
				+ "ORDER BY g.game_id DESC";

		SqlRowSet games = jdbcTemplate.queryForRowSet(sqlGetGames, userId);
		List<Game> userGames = new ArrayList<>();
		while (games.next()) {
			userGames.add(mapRowToGame(games, true));
		}
		return userGames;
	}

	private Game mapRowToGame(SqlRowSet row, boolean addPlayers) {
		Game game = new Game();
		game.setGameId(row.getLong("game_id"));
		game.setGameName(row.getString("game_name"));
		game.setCreatorId(row.getLong("creator_id"));
		game.setEndDate(row.getString("end_date"));
		game.setEnded(row.getBoolean("ended"));
		if (addPlayers) {
			game.setPlayerCount(row.getInt("player_count"));
		}
		return game;
	}

}
