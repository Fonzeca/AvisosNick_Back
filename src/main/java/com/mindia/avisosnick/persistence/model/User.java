package com.mindia.avisosnick.persistence.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@TypeAlias("User")
public class User {
	@Id
    private ObjectId id;
    private String email;
    private String passwordHash;
    private String uniqueMobileToken;
    
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
