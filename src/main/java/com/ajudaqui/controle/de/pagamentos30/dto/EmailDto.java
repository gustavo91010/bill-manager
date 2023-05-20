package com.ajudaqui.controle.de.pagamentos30.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EmailDto {
	
	@NotNull(message = "user_id não pode ser nulo")
	private Long user_id;
	@NotBlank
	@Email
	private String emailTo;

	@NotBlank
	private String subject;

	@NotBlank
	private String text;


	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "EmailDto [user_id=" + user_id + ", emailTo=" + emailTo + ", subject=" + subject + ", text=" + text
				+ "]";
	}

}
