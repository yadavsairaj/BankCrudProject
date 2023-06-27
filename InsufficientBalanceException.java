package com.sairaj.BankProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code=HttpStatus.NOT_FOUND)
public class InsufficientBalanceException extends RuntimeException {
	
	public InsufficientBalanceException(String message) {
		super(message);
	}
}
