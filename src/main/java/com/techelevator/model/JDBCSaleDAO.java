package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JDBCSaleDAO implements SaleDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCSaleDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void saveSale(Sale sale) {
		String sqlInsertSale = "INSERT INTO sale(game_id, user_id, purchase, stock_symbol, shares, price_per_share, transaction_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlInsertSale,
				sale.getGameId(), sale.getUserId(), sale.isPurchase(), sale.getStockSymbol(), sale.getShares(), sale.getPricePerShare(), sale.getTransactionDate());
	}

	@Override
	public List<Sale> getSalesByGameAndPlayer(Long gameId, Long userId) {
		String sqlGetSales = "SELECT * "
				+ "FROM sale "
				+ "WHERE user_id = ? "
				+ "AND game_id = ? "
				+ "ORDER BY transaction_date DESC";

		SqlRowSet playerSales = jdbcTemplate.queryForRowSet(sqlGetSales, userId, gameId);
		List<Sale> allPlayerSales = new ArrayList<>();
		while (playerSales.next()) {
			allPlayerSales.add(mapRowToSale(playerSales));
		}
		return allPlayerSales;
	}
	
	private Sale mapRowToSale(SqlRowSet row) {
		Sale sale = new Sale();
		sale.setUserId(row.getLong("user_id"));
		sale.setGameId(row.getLong("game_id"));
		sale.setStockSymbol(row.getString("stock_symbol"));
		sale.setShares(row.getLong("shares"));
		sale.setPurchase(row.getBoolean("purchase"));
		sale.setPricePerShare(row.getDouble("price_per_share"));
		sale.setTransactionDate(row.getDate("transaction_date"));
		return sale;
	}

}
