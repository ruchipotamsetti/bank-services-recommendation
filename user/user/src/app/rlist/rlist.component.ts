import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AutoLoan } from '../AutoLoan.model';
import { HomeLoan } from '../homeLoan.model';
import { PersonalLoan } from '../personalLoan.model';
import { RecommendationService } from '../recommendation.service';
import { RecommendForm } from '../RecommendForm.model';

@Component({
  selector: 'app-rlist',
  templateUrl: './rlist.component.html',
  styleUrls: ['./rlist.component.css']
})
export class RlistComponent {
  
    
  personalArray: PersonalLoan[] =[]
  homeArray: HomeLoan[] =[]
  autoArray: AutoLoan[] =[]

  
  loanType:string='';
  displayPersonal=false;
  displayHome=false;
  displayAuto=false;

    ngOnInit(){
       this.onLoanSelected();
    }
  
    constructor(private _router:Router){

    }
    
    
    viewMore(loan : any){



      console.log('below is loan obj')
      console.log(loan);
      console.log('above is loan obj')
      

      localStorage.setItem('selectedLoan',JSON.stringify(loan));
      this._router.navigate(['/viewdetails'])
      // this.reload();
      
    }

    selectedLoanArray: any[]=[];
  selectedLoan:string|null='';
    onLoanSelected() {  
      let type = localStorage.getItem("loanType"); 
      if(type != null){
        this.selectedLoan = localStorage.getItem("loanType")
      }
    
      switch (this.selectedLoan) {
        case 'Home Loan':
          const home = localStorage.getItem('home')+'';
          this.homeArray =JSON.parse(home);
          if(this.homeArray.length==0){
            alert("No recommendations to show!")
            this._router.navigate(['/recomform'])
          }
          this.selectedLoanArray = this.homeArray;
          break;
    
        case 'Personal Loan':
          const personal = localStorage.getItem('personal')+'';
          this.personalArray =JSON.parse(personal);
          if(this.personalArray.length==0){
            alert("No recommendations to show!")
            this._router.navigate(['/recomform'])
          }
          console.log("PersonalArray: "+this.personalArray+" hello")
          this.selectedLoanArray = this.personalArray;
          break;
    
        case 'Auto Loan':
          const auto = localStorage.getItem('auto')+'';
          this.autoArray =JSON.parse(auto);
          if(this.autoArray.length==0){
            alert("No recommendations to show!")
            this._router.navigate(['/recomform'])
          }
          this.selectedLoanArray = this.autoArray;
          break;
    
        default:
          console.error('Invalid loan type selected');
      }

      console.log(this.selectedLoanArray)
    }
    pageReload=false;
    reload(){
      if(!this.pageReload){
        this.pageReload=true;
        window.location.reload();
      }
    }
    
}
