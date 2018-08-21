package com.techelevator.model;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
		// TODO Auto-generated method stub
		return null;
	}

}
