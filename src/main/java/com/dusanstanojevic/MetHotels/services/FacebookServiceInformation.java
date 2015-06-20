package com.dusanstanojevic.MetHotels.services;

public class FacebookServiceInformation {
	private String actionToken;
	public boolean isLoggedIn() {
		return actionToken != null;
	}
	public String getAccessToken() {
		return actionToken;
	}
	public void setActionToken(String actionToken) {
		this.actionToken = actionToken;
	}
}
