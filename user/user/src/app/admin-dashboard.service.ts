import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Documentation} from './documentation.model';
import { LoanApplications } from './loanapplications.model';
import { Emi } from './emi.model';

@Injectable({
  providedIn: 'root'
})
export class AdminDashboardService {

  constructor(private _http:HttpClient) { }
  viewDocs(){
    return this._http.get<Documentation[]>('http://localhost:9999/recommend/getalldocs');
  }

  // approve(documentation:Documentation){
  //   return this._http.put<String>("http://localhost:9999/recommend/status",documentation);
  // }

  getApplications(){
    return this._http.get(`http://localhost:9999/recommend/getapplications`);
  }

  approve(loanApplication:LoanApplications){
    return this._http.put<String>('http://localhost:9999/recommend/approve', loanApplication, {responseType: 'text' as 'json'});
  }

  reject(loanApplication:LoanApplications){
    return this._http.put<String>('http://localhost:9999/recommend/reject', loanApplication, {responseType: 'text' as 'json'});
  }

  generateEmi(loanApplication: LoanApplications){
    return this._http.post<string>('http://localhost:9999/recommend/generateemi', loanApplication,{responseType: 'text' as 'json'});
  }

  getEmi(email:string, applicationId:number){
    return this._http.get<Emi[]>(`http://localhost:9999/recommend/getemi/${email}/${applicationId}`);
  }

  updateEmiStatus(emi:Emi){
    return this._http.put<String>(`http://localhost:9999/recommend/updateemi`, emi, {responseType: 'text' as 'json'});
    
  }

}
