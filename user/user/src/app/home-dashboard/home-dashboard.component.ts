import { Component } from '@angular/core';
import { HomedashboardService } from '../homedashboard.service';
import { Router } from '@angular/router';
import { LoanApplications } from '../loanapplications.model';
import { Emi } from '../emi.model';
import { AuthRequest } from '../authRequest.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-home-dashboard',
  templateUrl: './home-dashboard.component.html',
  styleUrls: ['./home-dashboard.component.css']
})
export class HomeDashboardComponent {

  constructor(private _homeSrv:HomedashboardService, private _router:Router, private _userSrv:UserService){}

  displayEmiTable=false;
  displayApplicationsTable = false;
  ngOnInit(): void {
    if(localStorage.getItem('email')){
      this.getAllApplications()
      this.getCreditScore()
    }
    else{
      alert("Please login to view your applications")
      this._router.navigate(['/login'])
    }
    
  }
  //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
  //Add 'implements OnInit' to the class.
  

  email:string='';
  applications: LoanApplications[] = [];
  getAllApplications(){
    this.email = localStorage.getItem("email")+'';
    console.log(this.email)
    this._homeSrv.getApplications(this.email).subscribe(
      data=>{
        this.applications = JSON.parse(JSON.stringify(data))

        console.log(data)
        console.log("this is ="+this.applications);
        if(this.applications.length == 0){
          this.displayApplicationsTable=false;
        }
        else{
          this.displayApplicationsTable=true;
        }
      },
      error=>{
        console.log(error)
      }
    )
  }

  emiTable:Emi[]=[];
  getEmiTable(email:string, applicationId:number){
    //console.log("Inside getEmiTable")
    this._homeSrv.getEmi(email, applicationId).subscribe(
      data=>{
        
        this.displayEmiTable=true
        console.log(data);
        this.emiTable = data;
        this.emiTable.sort((a, b) =>  a.emiNo - b.emiNo);
      },
      error=>{
        console.log(error);
      }
    )
  }

  updateEmiStatus(emi:Emi){

    let status = "Processing"
    emi.status = status;
    this._homeSrv.updateEmiStatus(emi).subscribe(
      data=>{
        console.log(data)
        //this.getEmiTable(email, applicationId);
      },
      error=>{
        console.log(error);
      }
    )


  }

  creditScore!:number;
  authRequest2 = new AuthRequest();
  getCreditScore(){

    this.authRequest2.email = localStorage.getItem('email')+'';
    this._userSrv.getUserById(this.authRequest2).subscribe(
      data=>{
        console.log(data)
        console.log('credit score: '+data.creditScore)
        localStorage.setItem('creditScore',data.creditScore.toString());
        this.creditScore = data.creditScore;
      },
      error=>{

        console.log(error)
      }
    )
  }
  
}
