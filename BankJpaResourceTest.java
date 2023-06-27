package com.sairaj.restfulwebservices.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sairaj.BankProject.exception.UserNotFoundException;
import com.sairaj.BankProject.jpa.BankRepository;
import com.sairaj.BankProject.service.BankServiceImpl;
import com.sairaj.BankProject.user.Bank;


//launch context for BLankJpaResource
@ExtendWith(MockitoExtension.class)
class BankJpaResourceTest {
	
		//@InjectMocks annotation is used to inject the BankServiceImpl instance into the test class.
		@InjectMocks
		private BankServiceImpl service;
	
		//@InjectMocks
		//private BankJpaResource bankJpaResource;
		
		//The @Mock annotation is used to create a mock instance of the BankRepository interface.
		@Mock
		private BankRepository repository;

		//get acc by id
		@Test
		public void testRetrieveUser(){
			
			
			Bank bank = new Bank();
			bank.setName("sai");
			bank.setAccountNumber(101);
			bank.setDepositAmount(10000);
			bank.setWithdrawAmount(200);
			bank.setBalance(8800);
			bank.setPermanentAddress("Kalyan");
			bank.setCommunicationAddress("domb");
			bank.setNotes("SomeNOtes");
			
	        // Mock the behavior of the repository findById method
			Mockito.when(repository.findById(101)).thenReturn(Optional.of(bank));
	        // Call the service method and assert the result
			assertEquals(service.retrieveUser(101),Optional.of(bank));
	
	}
		
	    // Test case for handling UserNotFoundException when retrieving a user
		@Test
		public void testRetrieveUser_UserNotFoundException() {
	        // Mock the behavior of the repository findById method to return an empty Optional
			Mockito.when(repository.findById(101)).thenReturn(Optional.empty());

			try {
	            // Call the service method and expect a UserNotFoundException to be thrown
				service.retrieveUser(101);
			} catch (UserNotFoundException ex) {
	            // Assert the error message in the exception
				assertEquals("Account Number 101 not found", ex.getMessage());
			}
		}	
		
	    // Test case for deleting a user
		@Test
		public void testDeleteUser() {
			int accountNumber = 101;
			Mockito.when(repository.existsById(accountNumber)).thenReturn(true);

			ResponseEntity<Bank> result = service.deleteUser(accountNumber);

			assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
			verify(repository, times(1)).deleteById(accountNumber);
		}
		
	    // Test case for handling UserNotFoundException when deleting a user
		@Test
		public void testDeleteUser_UserNotFoundException() {
			int accountNumber = 101;
			Mockito.when(repository.existsById(accountNumber)).thenReturn(false);

			try {
				service.deleteUser(accountNumber);
			} catch (UserNotFoundException ex) {
				assertEquals("Account Number 101 not found", ex.getMessage());
			}
		}
		
	    // Test case for creating a user
		@Test
		public void testCreateUser() {
			Bank bank = new Bank("sai", 101, 10000, 200, 8800, "Kalyan", "domb", "notes");
			Mockito.when(repository.save(Mockito.any(Bank.class))).thenReturn(bank);
			
			Bank newBankDetails = service.createUser(bank);
			
			assertNotNull(newBankDetails);
			assertEquals(bank.getAccountNumber(), newBankDetails.getAccountNumber());
	        // Verify that the repository save method is called
			Mockito.verify(repository).save(Mockito.any(Bank.class));
		}
		
	    // Test case for handling UserNotFoundException when creating a user with insufficient funds
		@Test
		public void testCreateUser_InsufficientFunds() {
	        // Create a Bank object with insufficient funds
			Bank bank = new Bank();
			bank.setName("sai");
			bank.setAccountNumber(101);
			bank.setDepositAmount(10000);
			bank.setWithdrawAmount(200);
			bank.setBalance(100);
			bank.setPermanentAddress("Kalyan");
			bank.setCommunicationAddress("domb");
			bank.setNotes("SomeNOtes");

			try {
	            // Call the service method and expect a UserNotFoundException to be thrown
				service.createUser(bank);
			} catch (UserNotFoundException ex) {
				assertEquals("Insufficient Funds", ex.getMessage());
			}
		}
		
	    // Test case for updating an account
		@Test
		public void testUpdateAccount() {
			int accountNumber = 102;
			Optional<Bank> bank = Optional.of(new Bank("Ashok", 102, 28000, 2000, 26000, "202, Ganesh krupa bldg, 100ft road, kalyan,maharashtra,421306", "602, Rais Aaragya, 100ft road, kalyan,maharashtra,421306", "somenotes"));
			Mockito.when(repository.findById(102)).thenReturn(bank);
			
			Bank updatedbank = new Bank("Ashok", 102, 28000, 2000, 26000, "202, Ganesh krupa bldg, 100ft road, kalyan,maharashtra,421306", "602, Rais Aaragya, 100ft road, kalyan,maharashtra,421306", "somenotes");
			
			Mockito.when(repository.existsById(accountNumber)).thenReturn(true);
			Mockito.when(repository.save(Mockito.any(Bank.class))).thenReturn(updatedbank);
			
			Bank result = null;
			try {
	            // Call the service method to update the account
				result = service.updateAccount(updatedbank, accountNumber);
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}
	        // Assert that the updated bank object is not null and has the same account number and balance as the updatedbank
			assertNotNull(result);
			assertEquals(updatedbank.getAccountNumber(), result.getAccountNumber());
			assertEquals(updatedbank.getBalance(), result.getBalance());
		}
		
		
	    // Test case for handling UserNotFoundException when updating an account
		@Test
		public void testUpdateAccount_UserNotFoundException() {
			int accountNumber = 101;
			Bank bank = new Bank();
			bank.setName("sai");
			bank.setAccountNumber(accountNumber);
			bank.setDepositAmount(10000);
			bank.setWithdrawAmount(200);
			bank.setBalance(8800);
			bank.setPermanentAddress("Kalyan");
			bank.setCommunicationAddress("domb");
			bank.setNotes("SomeNOtes");
			
			Mockito.when(repository.existsById(accountNumber)).thenReturn(false);

			try {
				service.updateAccount(bank, accountNumber);
			} catch (UserNotFoundException ex) {
				assertEquals("Account Number 101 not found", ex.getMessage());
			}
		} 
		
	    // Test case for handling UserNotFoundException when updating an account with insufficient funds
		@Test
		public void testUpdateAccount_InsufficientFunds() {
			int accountNumber = 101;
			Bank bank = new Bank();
			bank.setName("sai");
			bank.setAccountNumber(accountNumber);
			bank.setDepositAmount(10000);
			bank.setWithdrawAmount(200);
			bank.setBalance(100);
			bank.setPermanentAddress("Kalyan");
			bank.setCommunicationAddress("domb");
			bank.setNotes("SomeNOtes");
			
			Mockito.when(repository.existsById(accountNumber)).thenReturn(true);
			
			Mockito.when(repository.findById(accountNumber)).thenReturn(Optional.of(bank));

			try {
				service.updateAccount(bank, accountNumber);
			} catch (UserNotFoundException ex) {
				assertEquals("Insufficient Funds", ex.getMessage());
			}
		}
		

}
