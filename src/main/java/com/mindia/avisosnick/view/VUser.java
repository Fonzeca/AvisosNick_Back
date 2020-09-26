
package com.mindia.avisosnick.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "email",
    "password"
})
public class VUser {

    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    @JsonProperty("username")
    public String getEmail() {
        return email;
    }

    @JsonProperty("username")
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

}
