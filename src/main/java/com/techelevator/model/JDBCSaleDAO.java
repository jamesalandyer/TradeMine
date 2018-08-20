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
		// TODO Auto-generated method stub

	}

	@Override
	public List<Sale> getSalesByGameAndPlayer(Long gameId, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
