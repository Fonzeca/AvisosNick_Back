package com.mindia.avisosnick.persistence.model;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("AuthUser")
public class AuthUser {
	private String provider = "";
	private String lastIdToken = "";
	private String userId = "";
	private long expirationLastIdToken;
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getLastIdToken() {
		return lastIdToken;
	}
	public void setLastIdToken(String lastIdToken) {
		this.lastIdToken = lastIdToken;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getExpirationLastIdToken() {
		return expirationLastIdToken;
	}
	public void setExpirationLastIdToken(long expirationLastIdToken) {
		this.expirationLastIdToken = expirationLastIdToken;
	}
}
