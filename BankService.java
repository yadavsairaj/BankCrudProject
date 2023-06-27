package com.sairaj.BankProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.sairaj.BankProject.user.Bank;

public interface BankService {
	List<Bank> retrieveAllUser();
	Optional<Bank> retrieveUser(long accountNumber);
    Bank updateAccount(Bank updatedAccount, long accountNumber);
    ResponseEntity<Bank>  deleteUser(long accountNumber);
    Bank createUser(Bank bank);
	
	
}
