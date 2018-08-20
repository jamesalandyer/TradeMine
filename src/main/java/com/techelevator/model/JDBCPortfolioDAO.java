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
public class JDBCPortfolioDAO implements PortfolioDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCPortfolioDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void savePortfolio(Portfolio portfolio) {
		String sqlInsertPortfolio = "INSERT INTO portfolio VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sqlInsertPortfolio,
				portfolio.getUserId(), portfolio.getGameId(), portfolio.getStockSymbol(), portfolio.getShares());
	}
	
	@Override
	public void updatePortfolio(Portfolio portfolio) {
		jdbcTemplate.update("UPDATE portfolio SET shares = ? WHERE game_id = ? AND user_id = ? AND stock_symbol = ?",
				portfolio.getShares(), portfolio.getGameId(), portfolio.getUserId(), portfolio.getStockSymbol());
	}
	
	@Override
	public void removePortfolio(Portfolio portfolio) {
		jdbcTemplate.update("DELETE FROM portfolio WHERE game_id = ? AND user_id = ? AND stock_symbol = ?",
				portfolio.getGameId(), portfolio.getUserId(), portfolio.getStockSymbol());
	}

	@Override
	public List<Portfolio> getPortfoliosForGame(Long gameId, Long userId) {
		String sqlGetPortfolios = "SELECT * "
				+ "FROM portfolio "
				+ "WHERE user_id = ? "
				+ "AND game_id = ? "
				+ "ORDER BY shares DESC";

		SqlRowSet playerPortfolios = jdbcTemplate.queryForRowSet(sqlGetPortfolios, userId, gameId);
		List<Portfolio> allPlayerPortfolios = new ArrayList<>();
		while (playerPortfolios.next()) {
			allPlayerPortfolios.add(mapRowToPortfolio(playerPortfolios));
		}
		return allPlayerPortfolios;
	}
	
	private Portfolio mapRowToPortfolio(SqlRowSet row) {
		Portfolio portfolio = new Portfolio();
		portfolio.setUserId(row.getLong("user_id"));
		portfolio.setGameId(row.getLong("game_id"));
		portfolio.setStockSymbol(row.getString("stock_symbol"));
		portfolio.setShares(row.getLong("shares"));
		return portfolio;
	}

}
