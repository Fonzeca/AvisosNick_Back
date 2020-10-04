package com.mindia.avisosnick.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PojoIdNotice {
	
	@JsonProperty("idNotice")
	private String idNotice;

	@JsonProperty("idNotice")
	public String getIdNotice() {
		return idNotice;
	}

	@JsonProperty("idNotice")
	public void setIdNotice(String idNotice) {
		this.idNotice = idNotice;
	}

	
}
