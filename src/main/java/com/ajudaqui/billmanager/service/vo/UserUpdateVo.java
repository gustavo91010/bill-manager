package com.ajudaqui.billmanager.service.vo;

import java.time.LocalDateTime;

public class UserUpdateVo {
	private String name;
	private String email;
	private LocalDateTime updated_at ;
	
	
	public UserUpdateVo(String name, String email) {
		super();
		this.name = name;
		this.email = email;
		this.updated_at= LocalDateTime.now();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public LocalDateTime getUpdatedAt() {
		return updated_at;
	}


	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updated_at = updatedAt;
	}
	
	

}
