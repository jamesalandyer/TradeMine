package com.techelevator.model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JDBCPlayerDAO implements PlayerDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCPlayerDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void savePlayer(Player player) {
		String sqlInsertPlayer = "INSERT INTO player VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlInsertPlayer,
				player.getAmountLeft(), player.getInviterId(), false, player.getUserId(), player.getGameId());
	}
	
	@Override
	public void updatePlayer(Player player) {
		String sqlUpdatePlayer = "UPDATE player SET amount_left = ? WHERE game_id = ? AND user_id = ?";
		jdbcTemplate.update(sqlUpdatePlayer,
				player.getAmountLeft(), player.getGameId(), player.getUserId());
	}

	@Override
	public List<Player> getPlayersForGame(Long gameId) {
		String sqlGetPlayers = "SELECT p.amount_left, p.inviter_id, p.joined, p.user_id, p.game_id, u.user_name "
				+ "FROM player p "
				+ "INNER JOIN app_user u ON p.user_id = u.id "
				+ "WHERE game_id = ? "
				+ "ORDER BY p.amount_left ";

		SqlRowSet players = jdbcTemplate.queryForRowSet(sqlGetPlayers, gameId);
		List<Player> allPlayers = new ArrayList<>();
		while (players.next()) {
			allPlayers.add(mapRowToPlayer(players, true, false));
		}
		return allPlayers;
	}

	@Override
	public List<Player> getInvitesForUser(Long userId) {
		String sqlGetPlayers = "SELECT * "
				+ "FROM player p "
				+ "INNER JOIN game g ON g.game_id = p.game_id "
				+ "INNER JOIN app_user u ON p.inviter_id = u.id "
				+ "WHERE user_id = ? "
				+ "AND joined = false";

		SqlRowSet players = jdbcTemplate.queryForRowSet(sqlGetPlayers, userId);
		List<Player> allPlayers = new ArrayList<>();
		while (players.next()) {
			allPlayers.add(mapRowToPlayer(players, true, true));
		}
		return allPlayers;
	}

	@Override
	public Player getPlayerForGame(Long userId, Long gameId) {
		String sqlGetPlayers = "SELECT * "
				+ "FROM player p "
				+ "INNER JOIN game g ON g.game_id = p.game_id "
				+ "WHERE user_id = ? "
				+ "AND p.game_id = ?";

		SqlRowSet player = jdbcTemplate.queryForRowSet(sqlGetPlayers, userId, gameId);
		if (player.next()) {
			return mapRowToPlayer(player, false, true);
		}
		return null;
	}
	
	@Override
	public List<String> getPlayerBalanceForGames(Long userId) {
		String sqlGetPlayers = "SELECT * "
				+ "FROM player "
				+ "WHERE user_id = ? "
				+ "ORDER BY game_id DESC";

		SqlRowSet gameBalances = jdbcTemplate.queryForRowSet(sqlGetPlayers, userId);
		List<String> allGameBalances = new ArrayList<>();
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		while (gameBalances.next()) {
			allGameBalances.add(formatter.format(gameBalances.getBigDecimal("amount_left")));
		}
		return allGameBalances;
	}

	@Override
	public void acceptInvite(Player invite) {
		jdbcTemplate.update("UPDATE player SET joined = true WHERE game_id = ? AND user_id = ?",
				invite.getGameId(), invite.getUserId());
	}

	@Override
	public void declineInvite(Player invite) {
		jdbcTemplate.update("DELETE FROM player WHERE game_id = ? AND user_id = ?",
				invite.getGameId(), invite.getUserId());
	}

	private Player mapRowToPlayer(SqlRowSet row, boolean userName, boolean gameName) {
		Player player = new Player();
		player.setAmountLeft(row.getBigDecimal("amount_left"));
		player.setInviterId(row.getLong("inviter_id"));
		player.setJoined(row.getBoolean("joined"));
		player.setUserId(row.getLong("user_id"));
		player.setGameId(row.getLong("game_id"));
		if (userName) {
			player.setUserName(row.getString("user_name"));
		}
		if (gameName) {
			player.setGameName(row.getString("game_name"));
		}
		return player;
	}
	
}
