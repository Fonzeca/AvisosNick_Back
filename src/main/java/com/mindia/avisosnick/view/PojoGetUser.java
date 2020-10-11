
package com.mindia.avisosnick.view;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mail",
    "fullName",
    "roles",
    "userType"
})
public class PojoGetUser {

    @JsonProperty("mail")
    private String mail;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("roles")
    private List<String> roles = null;
    @JsonProperty("userType")
    private List<String> userType = null;

    @JsonProperty("mail")
    public String getMail() {
        return mail;
    }

    @JsonProperty("mail")
    public void setMail(String mail) {
        this.mail = mail;
    }

    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    @JsonProperty("fullName")
    public void setFullName(String fullName) {
        this.fullName = fullName;
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

}
