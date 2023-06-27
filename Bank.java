package com.sairaj.BankProject.user;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

//to let jpa manage this we use Entity annotation
@Entity(name="bank_details")
public class Bank {
	@Size(min=2, message=" Name is absent or have less than two characters")
	@NotNull
	private String name;
	
	public Bank() {}
	
	//identifier
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "my_entity_seq_gen")
    @SequenceGenerator(name = "my_entity_seq_gen", sequenceName = "my_entity_seq", allocationSize = 1, initialValue = 105)
	private Integer accountNumber;
	
	@PositiveOrZero(message=" Deposit amount should be positive")
	private Integer depositAmount;
	@PositiveOrZero(message=" Withdraw amount should be positive or zero")
	private Integer withdrawAmount;
	@PositiveOrZero(message=" Balance amount should be positive or zero")
	private Integer balance;
	@Size(max=250)
	private String permanentAddress;
	@Size(max=250)
	private String communicationAddress;
	@Size(max=250)
	private String notes;
	
	//constructor
	public Bank(String name, Integer accountNumber, Integer depositAmount, Integer withdrawAmount, Integer balance, String permanentAddress, String communicationAddress, String notes) {
		super();
		this.name = name;
		this.accountNumber = accountNumber;
		this.depositAmount = depositAmount;
		this.withdrawAmount = withdrawAmount;
		this.balance = balance;
		this.permanentAddress = permanentAddress;
		this.communicationAddress = communicationAddress;
		this.notes = notes;
	}
	
	//getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Integer getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(long newDepositAmount) {
		this.depositAmount = (int) newDepositAmount;
	}
	public Integer getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(Integer withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(long balanceAmount) {
		this.balance = (int) balanceAmount;
	}
	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getCommunicationAddress() {
		return communicationAddress;
	}

	public void setCommunicationAddress(String communicationAddress) {
		this.communicationAddress = communicationAddress;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	@Override
	public String toString() {
		return "Bank [name=" + name + ", accountNumber=" + accountNumber + ", depositAmount=" + depositAmount
				+ ", withdrawAmount=" + withdrawAmount + ", balance=" + balance + ", permanentAddress="
				+ permanentAddress + ", communicationAddress=" + communicationAddress + ", notes=" + notes + "]";
	}

	
	

	
}
