package com.sairaj.BankProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sairaj.BankProject.exception.InsufficientBalanceException;
import com.sairaj.BankProject.exception.UserNotFoundException;
import com.sairaj.BankProject.jpa.BankRepository;
import com.sairaj.BankProject.user.Bank;

@Service
public class BankServiceImpl implements BankService{
	private BankRepository bankRepository;
	public BankServiceImpl(BankRepository bankRepository) {
		this.bankRepository = bankRepository;
	}

	
	/**
	 * Retrieves all Bank account details
	 */
	@Override
	public List<Bank> retrieveAllUser() {
		return bankRepository.findAll();
	}
	/**
	 * Retrieves an Bank account by its ID.
	 *
	 * @throws NotFoundException if ID provided does not match with the account number in database
	 */
	@Override
	public Optional<Bank> retrieveUser(long accountNumber) {
		Optional <Bank> details = bankRepository.findById((int) accountNumber);
		if (!details.isPresent()) {
			throw new UserNotFoundException("Account Number " + accountNumber + " not found");
		}
		return Optional.ofNullable(details.get()); 
	}
	/**
	 * Updates an existing entity.
	 *
	 
	 * @return The updated account details
	 * @throws InsufficientBalanceException 
	 * @throws NotFoundException if ID provided does not match with the account number in database
	 * @throws InvalidDataException if withdraw amount is greater than balance amount
	 */
	@Override
    public Bank updateAccount(Bank updatedAccount, long accountNumber) throws InsufficientBalanceException  {
        
		boolean exists = bankRepository.existsById((int) accountNumber);
		if (!exists)
			throw new UserNotFoundException("Account Number " + accountNumber + " not found");

		Bank oldUserDetails = bankRepository.findById((int) accountNumber).get();

		oldUserDetails.setName(updatedAccount.getName());

		oldUserDetails.setCommunicationAddress(updatedAccount.getCommunicationAddress());			
		oldUserDetails.setPermanentAddress(updatedAccount.getPermanentAddress());
		oldUserDetails.setNotes(updatedAccount.getNotes());

		long newDepositAmount = oldUserDetails.getBalance() + updatedAccount.getDepositAmount();
		oldUserDetails.setDepositAmount(newDepositAmount);

		if (updatedAccount.getWithdrawAmount() <= (oldUserDetails.getBalance() + updatedAccount.getDepositAmount())) {
			oldUserDetails.setWithdrawAmount(updatedAccount.getWithdrawAmount());
			long balanceAmount = oldUserDetails.getBalance() + updatedAccount.getDepositAmount() - updatedAccount.getWithdrawAmount();
			oldUserDetails.setBalance(balanceAmount);
		} else {
			throw new InsufficientBalanceException("You have insufficient Balance to withdraw " + updatedAccount.getWithdrawAmount()
					+ " Your balance is : " + oldUserDetails.getBalance());
		}

		Bank updatedUser= bankRepository.save(oldUserDetails);
		
		return updatedUser;
		
    }

	/**
	 * Deletes an entity by its ID.
	 * @return 
	 * @return 
	  
	 * @throws NotFoundException if ID provided does not match with the account number in database
	 */
	@Override
	public ResponseEntity<Bank> deleteUser(long accountNumber) {
		boolean exists = bankRepository.existsById((int) accountNumber);
		if(!exists)
			throw new UserNotFoundException("Account Number " + accountNumber + " not found");
		
		bankRepository.deleteById((int) accountNumber);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		

	}

	/**
	 * Creates a new Bank account.
	 *
	 */
	@Override
	public Bank createUser(Bank bank) {
		int balance = bank.getDepositAmount()-bank.getWithdrawAmount();
		bank.setBalance(balance);
		if(bank.getWithdrawAmount()>bank.getBalance()) {
			throw new UserNotFoundException("Insufficient Funds");
		}
		return bankRepository.save(bank);
		
		
		
	}
	
	

	
}
