package com.techelevator.model;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
		// TODO Auto-generated method stub

	}

	@Override
	public List<Portfolio> getPortfoliosForGame(Long gameId, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
