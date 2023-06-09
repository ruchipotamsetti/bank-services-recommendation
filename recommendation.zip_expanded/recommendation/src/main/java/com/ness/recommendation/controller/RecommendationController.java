package com.ness.recommendation.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.ness.recommendation.model.AutoLoan;
import com.ness.recommendation.model.Documentation;
import com.ness.recommendation.model.Emi;
import com.ness.recommendation.model.HomeLoan;
import com.ness.recommendation.model.LoanApplications;
import com.ness.recommendation.model.PersonalLoan;
import com.ness.recommendation.model.RecommendationForm;
import com.ness.recommendation.model.SaveLoanData;
import com.ness.recommendation.repo.AutoLoanRepo;
import com.ness.recommendation.repo.HomeLoanRepo;
import com.ness.recommendation.repo.PersonalLoanRepo;
import com.ness.recommendation.service.AutoLoanService;
import com.ness.recommendation.service.DocumentationService;
import com.ness.recommendation.service.EmiService;
import com.ness.recommendation.service.HomeLoanService;
import com.ness.recommendation.service.LoanApplicationsService;
import com.ness.recommendation.service.PersonalLoanService;

@CrossOrigin
@RestController
@RequestMapping("recommend")
public class RecommendationController {

	@Autowired
	PersonalLoanService personalLoanService;
	@Autowired
	HomeLoanService homeLoanService;
	@Autowired
	AutoLoanService autoLoanService;
	@Autowired
	DocumentationService documentationService;
	@Autowired
	LoanApplicationsService loanApplicationsService;
	@Autowired
	EmiService emiService;
	@Autowired
	PersonalLoanRepo personalLoanRepo;
	@Autowired
	HomeLoanRepo homeLoanRepo;
	@Autowired
	AutoLoanRepo autoLoanRepo;
	
	//------------Personal Loan------------------------
	
//	@GetMapping("personalbyrequest/{interestrate}/{loanamt}/{occupationType}/{tenure}/{salary}")
//	public ResponseEntity<List<PersonalLoan>> getPersonalLoans(@PathVariable double interestrate,@PathVariable int loanamt,@PathVariable String occupationType,@PathVariable int tenure,@PathVariable int salary){
//		return new ResponseEntity<List<PersonalLoan>>(personalLoanService.getByRequest(interestrate,loanamt,occupationType,tenure,salary), HttpStatus.OK);
//	}
	
	@PostMapping("personalloanbyrequest")
	public ResponseEntity<List<PersonalLoan>> getPersonalLoans(@RequestBody RecommendationForm recommendationForm){
		return new ResponseEntity<List<PersonalLoan>>(personalLoanService.getByRequestByForm(recommendationForm), HttpStatus.OK);
	}
	
	@GetMapping("allpersonalloans")
	public ResponseEntity<List<PersonalLoan>> getAllPersonalLoans(){
	return new ResponseEntity<List<PersonalLoan>>(personalLoanService.getAll(), HttpStatus.OK	);
}
	
	//------------HomeLoan--------------------
	
	@PostMapping("homeloanbyrequest")
	public ResponseEntity<List<HomeLoan>> getHomeLoans(@RequestBody RecommendationForm recommendationForm){
		return new ResponseEntity<List<HomeLoan>>(homeLoanService.getByRequestByForm(recommendationForm), HttpStatus.OK);
	}
	
	@GetMapping("allhomeloans")
	public ResponseEntity<List<HomeLoan>> getAllHomeLoans(){
	return new ResponseEntity<List<HomeLoan>>(homeLoanService.getAll(), HttpStatus.OK);
	}
	
	//------------Auto Loan------------------------
	
	@PostMapping("autoloanbyrequest/{autoType}")
	public ResponseEntity<List<AutoLoan>> getAutoLoans(@RequestBody RecommendationForm recommendationForm, @PathVariable String autoType){
		return new ResponseEntity<List<AutoLoan>>(autoLoanService.getByRequestByForm(recommendationForm, autoType), HttpStatus.OK);
	}
	
	@GetMapping("allautoloans")
	public ResponseEntity<List<AutoLoan>> getAllAutoLoans(){
	return new ResponseEntity<List<AutoLoan>>(autoLoanService.getAll(), HttpStatus.OK);
	}
	
	
	//------------Documentation------------------------
	
	@PostMapping("uploaddocs/{email}")
	public ResponseEntity<Documentation> saveDocumentation(@PathVariable String email, @RequestBody Documentation documentation){
		return new ResponseEntity<Documentation>(documentationService.saveDocumentation(email, documentation), HttpStatus.OK);
	}
	
	@GetMapping("getalldocs")
	public ResponseEntity<List<Documentation>> getAllDocumentation(){
		return new ResponseEntity<List<Documentation>>(documentationService.getAllDocumentation(), HttpStatus.OK);
	}
	
	//update status of documentation to approve or reject by admin
	
	//------------Loan Applications------------------------
	
	@PostMapping("saveloanapplication")
	public ResponseEntity<LoanApplications> saveLoanApplication(@RequestBody LoanApplications loanApplication){
		System.out.println(loanApplication.getInterestRate());
		return new ResponseEntity<LoanApplications>(loanApplicationsService.saveApplication(loanApplication), HttpStatus.OK);
	}
	
	@GetMapping("getapplications")
	public ResponseEntity<List<LoanApplications>> getAllApplications(@RequestParam (required=false) String email){
		if(email!=null) {
			return new ResponseEntity<List<LoanApplications>>(loanApplicationsService.findByEmail(email), HttpStatus.OK);
		}
		return new ResponseEntity<List<LoanApplications>>(loanApplicationsService.getAllApplications(), HttpStatus.OK);
	}
	
	@PutMapping("approve")
	public ResponseEntity<String> updateAsApproved(@RequestBody LoanApplications loanApplication){
		
		String update = loanApplicationsService.updateAsApproved(loanApplication.getApplicationId());
		return new ResponseEntity<String>(update, HttpStatus.OK);
	}
	
	@PutMapping("reject")
	public ResponseEntity<String> updateAsRejected(@RequestBody LoanApplications loanApplication){
		
		String update = loanApplicationsService.updateAsRejected(loanApplication.getApplicationId());
		return new ResponseEntity<String>(update, HttpStatus.OK);
	}
	
	//------------EMIs------------------------
	
	@PostMapping("generateemi")
	public String generateEmis(@RequestBody LoanApplications loanApplication) {
		return emiService.generateEmis(loanApplication);
	}
	
	@GetMapping("getemibyemail/{email}")
	public ResponseEntity<List<Emi>> getEmisByEmail(@PathVariable String email){
		return new ResponseEntity<List<Emi>>(emiService.getEmisByEmail(email), HttpStatus.OK);
	}
	
	
	@GetMapping("getemi/{email}/{applicationId}")
	public ResponseEntity<List<Emi>> getEmisByEmail(@PathVariable String email, @PathVariable int applicationId){
		return new ResponseEntity<List<Emi>>(emiService.getEmisByEmailAndApplicationId(email, applicationId), HttpStatus.OK);
	}
	
	@PutMapping("updateemi")
	public ResponseEntity<String> updateEmiStatus(@RequestBody Emi emi){
		
		String update = emiService.updateEmiStatus(emi.getEmiId(), emi.getStatus(), emi.getEmail());
		return new ResponseEntity<String>(update, HttpStatus.OK);
	}

	
	
	
	
//	@GetMapping("personalbyrequest/{interestrate}")
//	public ResponseEntity<List<PersonalLoan>> getPersonalLoansInt(@PathVariable double interestrate){
//		
//		return new ResponseEntity<List<PersonalLoan>>(personalLoanService.intRateTest(interestrate), HttpStatus.OK);
//	}

//	@GetMapping("allHome")
//	public ResponseEntity<List<HomeLoan>> getAllHomeLoans(){
//	return new ResponseEntity<List<HomeLoan>>(personalLoanService.getAll(), HttpStatus.OK	);
//}
	
	
	
	
	 @PostMapping("saveLoan/{loanType}")
	    public String savedata(@RequestBody SaveLoanData saveLoanData, @PathVariable String loanType){
	    	if(loanType.equals("Personal Loan")) {
	    		PersonalLoan personalLoan = new PersonalLoan();
	    		personalLoan.setBankName(saveLoanData.getBankName());
	    		personalLoan.setContact(saveLoanData.getContact());
	    		personalLoan.setDescription(saveLoanData.getDescription());
	    		personalLoan.setEmiPerMonth(saveLoanData.getEmiPerMonth());
	    		personalLoan.setImage(saveLoanData.getImage());
	    		personalLoan.setInterestRate(saveLoanData.getInterestRate());
	    		personalLoan.setLoanId(saveLoanData.getLoanId());
	    		personalLoan.setMaxLoanAmt(saveLoanData.getMaxLoanAmt());
	    		personalLoan.setMaxTenure(saveLoanData.getMaxTenure());
	    		personalLoan.setMinCreditScore(saveLoanData.getMinCreditScore());
	    		personalLoan.setMinSalaryForSalaried(saveLoanData.getMinSalaryForSalaried());
	    		personalLoan.setMinSalaryForSelfEmployed(saveLoanData.getMinSalaryForSelfEmployed());
	    		personalLoan.setPrePaymentCharges(saveLoanData.getPrePaymentCharges());
	    		personalLoan.setProcessingFee(saveLoanData.getProcessingFee());
	    		personalLoan.setUrl(saveLoanData.getUrl());	
	    		personalLoanRepo.save(personalLoan);
	    		return "Personal Loan saved";
	    	}
	    	else if(loanType.equals("Home Loan")) {
	    		HomeLoan homeLoan=new HomeLoan();
	    		homeLoan.setBankName(saveLoanData.getBankName());
	    		homeLoan.setContact(saveLoanData.getContact());
	    		homeLoan.setDescription(saveLoanData.getDescription());
	    		homeLoan.setEmiPerMonth(saveLoanData.getEmiPerMonth());
	    		homeLoan.setImage(saveLoanData.getImage());
	    		homeLoan.setInterestRate(saveLoanData.getInterestRate());
	    		homeLoan.setLoanId(saveLoanData.getLoanId());
	    		homeLoan.setMaxLoanAmt(saveLoanData.getMaxLoanAmt());
	    		homeLoan.setMaxTenure(saveLoanData.getMaxTenure());
	    		homeLoan.setMinCreditScore(saveLoanData.getMinCreditScore());
	    		homeLoan.setMinSalaryForSalaried(saveLoanData.getMinSalaryForSalaried());
	    		homeLoan.setMinSalaryForSelfEmployed(saveLoanData.getMinSalaryForSelfEmployed());
	    		homeLoan.setPrePaymentCharges(saveLoanData.getPrePaymentCharges());
	    		homeLoan.setProcessingFee(saveLoanData.getProcessingFee());
	    		homeLoan.setUrl(saveLoanData.getUrl());
	    		homeLoanRepo.save(homeLoan);
	    		return "Home Loan saved";
	    		
	    	}
	    	else if(loanType.equals("Auto Loan"))
	    	{
	    		AutoLoan autoLoan=new AutoLoan();
	    		autoLoan.setAutoType(saveLoanData.getAutoType());
	    		autoLoan.setBankName(saveLoanData.getBankName());
	    		autoLoan.setContact(saveLoanData.getContact());
	    		autoLoan.setDescription(saveLoanData.getDescription());
	    		autoLoan.setEmiPerMonth(saveLoanData.getEmiPerMonth());
	    		autoLoan.setImage(saveLoanData.getImage());
	    		autoLoan.setInterestRate(saveLoanData.getInterestRate());
	    		autoLoan.setLoanId(saveLoanData.getLoanId());
	    		autoLoan.setMaxLoanAmt(saveLoanData.getMaxLoanAmt());
	    		autoLoan.setMaxTenure(saveLoanData.getMaxTenure());
	    		autoLoan.setMinCreditScore(saveLoanData.getMinCreditScore());
	    		autoLoan.setMinSalaryForSalaried(saveLoanData.getMinSalaryForSalaried());
	    		autoLoan.setMinSalaryForSelfEmployed(saveLoanData.getMinSalaryForSelfEmployed());
	    		autoLoan.setPrePaymentCharges(saveLoanData.getPrePaymentCharges());
	    		autoLoan.setProcessingFee(saveLoanData.getProcessingFee());
	    		autoLoan.setUrl(saveLoanData.getUrl());
	    		autoLoanRepo.save(autoLoan);
	    		return "auto loan saved";
	    	}
	    		
	    	return "No loan saved";
	          }

}
