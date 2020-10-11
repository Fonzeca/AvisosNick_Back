
package com.mindia.avisosnick.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mail",
    "fullName"
})
public class PojoUser {

    @JsonProperty("mail")
    private String mail;
    @JsonProperty("fullName")
    private String fullName;

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



}
