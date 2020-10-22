package com.mindia.avisosnick.persistence.model;

import java.util.List;

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
    private List<String> roles;
    private List<String> userType;
    private String fullName;
    private AuthUser auth;
    private boolean active;
    
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
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public List<String> getUserType() {
		return userType;
	}
	public void setUserType(List<String> userType) {
		this.userType = userType;
	}
	public AuthUser getAuth() {
		return auth;
	}
	public void setAuth(AuthUser auth) {
		this.auth = auth;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
