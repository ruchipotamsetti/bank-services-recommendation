package com.ness.recommendation.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplications {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	int applicationId;
	String email;
	String loanType;
    String loanId;
    double interestRate;
    int loanAmount;
    String bankName;
    String status;
    int tenure;
    LocalDate appliedOn;
    double emiPerMonth;

}
