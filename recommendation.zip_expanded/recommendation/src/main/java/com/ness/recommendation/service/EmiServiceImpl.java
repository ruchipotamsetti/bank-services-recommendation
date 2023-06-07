package com.ness.recommendation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.ness.recommendation.model.Emi;
import com.ness.recommendation.model.LoanApplications;
import com.ness.recommendation.repo.EmiRepo;

@Service
public class EmiServiceImpl implements EmiService{
	
	@Autowired
	EmiRepo emirepo;
	List<Emi> emiList = new ArrayList<Emi>();
    
	@Override
	public String generateEmis(LoanApplications loanApplication) {
		// TODO Auto-generated method stub
		
		// Calculate monthly interest rate
        double monthlyInterestRate = loanApplication.getInterestRate()/100/12;

        
     // Calculate EMI (Equated Monthly Installment)
        int emi = (int) calculateEMI(loanApplication.getLoanAmount(), monthlyInterestRate, loanApplication.getTenure()*12);
        
        int outstandingBalance = loanApplication.getLoanAmount();
        LocalDate date = loanApplication.getAppliedOn();
        for (int month = 1; month <= loanApplication.getTenure()*12; month++) {
        	Emi emiDetails = new Emi();
            	
        	emiDetails.setEmiId(loanApplication.getApplicationId()+""+month);
        	
        	emiDetails.setApplicationId(loanApplication.getApplicationId());
            emiDetails.setEmail(loanApplication.getEmail());
            emiDetails.setEmiNo(month);
            emiDetails.setLoanId(loanApplication.getLoanId());
            //date
            date = date.plusMonths(1);
            emiDetails.setDateOfPayment(date);
            
        	int monthlyInterest = (int) (outstandingBalance * monthlyInterestRate);
        	emiDetails.setMonthlyInterest(monthlyInterest);
        	
            int principal = (int) (emi - monthlyInterest);
            emiDetails.setPrincipal(principal);
            
            if(month==1) {
            	emiDetails.setBeginningLoanBalance(loanApplication.getLoanAmount());
            }
            else {
            	emiDetails.setBeginningLoanBalance(outstandingBalance);
            }
            outstandingBalance -= principal;
            
            emiDetails.setEmi(emi);
            emiDetails.setOutstandingBalance(outstandingBalance);
            emiDetails.setStatus("Unpaid");
            
            System.out.println(month+" "+emiDetails);
         
            emiList.add(emiDetails);
            
            
            
        }
        System.out.println(emiList.size());
       
        
        
        
        List<Emi> newList=new ArrayList<>();
        newList.addAll(emiList);
        System.out.println(newList.size());
        
        
        
        
        
//        newList.add(new Emi("myid", 100, "aa","aa",LocalDate.now(), 0, 1, 0, 11, 1, 5,""));
        
        
        emirepo.saveAll(newList);
        
        
		return "Generated emis for Application "+loanApplication.getApplicationId();
	}
	
	public static double calculateEMI(int loanAmount, double monthlyInterestRate, int loanTenureMonths) {
        double onePlusInterestRatePower = Math.pow(1 + monthlyInterestRate, loanTenureMonths);
        double emi = loanAmount * monthlyInterestRate * onePlusInterestRatePower /
                (onePlusInterestRatePower - 1);
        return emi;
    }
}