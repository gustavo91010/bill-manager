package com.ajudaqui.billmanager.response;

import com.ajudaqui.billmanager.entity.Payment;

public class ApiPayment {
	private Payment payment;
	

	public ApiPayment(Payment payment) {
		setPayment(payment);
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	

}
