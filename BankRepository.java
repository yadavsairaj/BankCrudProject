package com.sairaj.BankProject.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sairaj.BankProject.user.Bank;

public interface BankRepository extends JpaRepository<Bank, Integer> {

}
