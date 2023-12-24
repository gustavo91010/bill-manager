package com.ajudaqui.billmanager.response;

import java.util.ArrayList;
import java.util.List;

import com.ajudaqui.billmanager.entity.Payment;

public class ApiPayments {
	private List<Payment> payments = new ArrayList<Payment>();

	public ApiPayments(List<Payment> payments) {

		setPayments(payments);
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

}
