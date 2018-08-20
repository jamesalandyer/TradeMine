package com.techelevator.model;

public class Portfolio {
	private Long userId;
	private Long gameId;
	private String stockSymbol;
	private Long shares;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public Long getShares() {
		return shares;
	}
	public void setShares(Long shares) {
		this.shares = shares;
	}
	
	public Double getSharesValue(Double pricePerShare) {
		return shares * pricePerShare;
	}
}
