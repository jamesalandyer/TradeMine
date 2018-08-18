package com.techelevator.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Player {
	private BigDecimal amountLeft;
	private Long userId;
	private String userName;
	private Long gameId;
	private String gameName;
	private Long inviterId;
	private boolean joined;
	
	public BigDecimal getAmountLeft() {
		return amountLeft;
	}
	public String getMoneyLeft() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(amountLeft);
	}
	public void setAmountLeft(BigDecimal amountLeft) {
		this.amountLeft = amountLeft;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public Long getInviterId() {
		return inviterId;
	}
	public void setInviterId(Long inviterId) {
		this.inviterId = inviterId;
	}
	public boolean isJoined() {
		return joined;
	}
	public void setJoined(boolean joined) {
		this.joined = joined;
	}
}
