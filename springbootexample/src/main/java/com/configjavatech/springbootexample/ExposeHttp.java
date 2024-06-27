package com.configjavatech.springbootexample;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ExposeHttp {

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
