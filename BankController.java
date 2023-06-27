package com.sairaj.BankProject.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sairaj.BankProject.exception.InsufficientBalanceException;
import com.sairaj.BankProject.exception.UserNotFoundException;
import com.sairaj.BankProject.service.BankServiceImpl;
import com.sairaj.BankProject.user.Bank;

import jakarta.validation.Valid;

@RestController
public class BankController {
	@Autowired
	private BankServiceImpl service;
	
	 /**
     * Retrieve all users.
     *
     * @return List of Bank objects representing all users.
     */
	@GetMapping("/jpa/users")
	public List<Bank> retrieveAllUser() {
		return service.retrieveAllUser();
	}

	/**
     * Retrieve a user by account number.
     *
     * @param accountNumber The account number of the user to retrieve.
     * @return Optional of Bank representing the user if found.
     * @throws UserNotFoundException if the user with the given account number is not found.
     */
	//accept path parameters and to be able to capture the value of path parameter we use annotation of path variable 
	@GetMapping("/jpa/users/{accountNumber}")
	public Optional<Bank> retrieveUser(@PathVariable int accountNumber) {
		Optional<Bank> bank = service.retrieveUser(accountNumber);

		if (bank.isEmpty())
			throw new UserNotFoundException("accountNumber:" + accountNumber + "Not Found");
		return bank;
	}

	
	 /**
     * Delete a user by account number.
     *
     * @param accountNumber The account number of the user to delete.
     * @return ResponseEntity with HTTP status 202 (Accepted) if deletion is successful.
     */
	@DeleteMapping("/jpa/users/{accountNumber}")
	public ResponseEntity<Bank> deleteUser(@PathVariable Integer accountNumber) {
		service.deleteUser(accountNumber);

		return ResponseEntity.accepted().build();

	}

	/**
     * Create a new user.
     *
     * @param bank The Bank object representing the user to create.
     * @return ResponseEntity with HTTP status 201 (Created) and the location of the created user in the response headers.
     */
	//Request content using annotation request body
	//When creating resource it is better to send response status as created
	//Add dependency of starter-validation-@Valid accepts parameter, however add validations in bean 
	@PostMapping("/jpa/users")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createUser(@Valid @RequestBody Bank bank) {
		Bank savedUser = service.createUser(bank);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{accountNumber}")
				.buildAndExpand(savedUser.getAccountNumber()).toUri();
		return ResponseEntity.created(location).build();
	}

	/**
     * Update the account details of a user.
     *
     * @param accountNumber The account number of the user to update.
     * @param bank          The Bank object representing the updated account details.
     * @return ResponseEntity with HTTP status 201 (Created) and the location of the updated user in the response headers.
	 * @throws InsufficientBalanceException 
     */
	@PutMapping("/jpa/users/{accountNumber}")
	public ResponseEntity<Object> updateAccount(@PathVariable int accountNumber, @Valid @RequestBody Bank bank) throws InsufficientBalanceException {
		service.updateAccount(bank, accountNumber);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(location).build();
	}

}
