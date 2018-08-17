package com.techelevator.model;

import java.math.BigDecimal;

public class Player {
	private BigDecimal amountLeft;
	private Long userId;
	private Long gameId;
	private Long inviterId;
	private boolean joined;
	
	public BigDecimal getAmountLeft() {
		return amountLeft;
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
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
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
