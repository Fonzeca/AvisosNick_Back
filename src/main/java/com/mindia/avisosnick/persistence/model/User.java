package com.mindia.avisosnick.persistence.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class User {
	@Id
    private ObjectId id;
    private String username;
    private String passwordHash;
    private String uniqueMobileToken;
    
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getUniqueMobileToken() {
		return uniqueMobileToken;
	}
	public void setUniqueMobileToken(String uniqueMobileToken) {
		this.uniqueMobileToken = uniqueMobileToken;
	}
}
