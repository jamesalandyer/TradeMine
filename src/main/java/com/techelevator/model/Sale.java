package com.techelevator.model;

import java.text.NumberFormat;
import java.util.Date;

public class Sale {
	private Long saleId;
	private Long gameId;
	private Long userId;
	private boolean purchase;
	private String stockSymbol;
	private Long shares;
	private Double pricePerShare;
	private Date transactionDate;
	
	public Long getSaleId() {
		return saleId;
	}
	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public boolean isPurchase() {
		return purchase;
	}
	public void setPurchase(boolean purchase) {
		this.purchase = purchase;
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
	public Double getPricePerShare() {
		return pricePerShare;
	}
	public void setPricePerShare(Double pricePerShare) {
		this.pricePerShare = pricePerShare;
	}
	public String getPricePerShareFormatted() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(pricePerShare);
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
}
