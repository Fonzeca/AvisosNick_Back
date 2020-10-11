
package com.mindia.avisosnick.view;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "email",
    "password",
    "roles",
    "userType"
})
public class VUser {

    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("roles")
    private List<String> roles = null;
    @JsonProperty("userType")
    private List<String> userType = null;
    @JsonProperty("fullName")
    private String fullName = null;

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles;
    }

    @JsonProperty("roles")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @JsonProperty("userType")
    public List<String> getUserType() {
        return userType;
    }

    @JsonProperty("userType")
    public void setUserType(List<String> userType) {
        this.userType = userType;
    }
    
    @JsonProperty("fullName")
	public String getFullName() {
		return fullName;
	}
    
    @JsonProperty("fullName")
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
