import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Documentation} from './documentation.model';
import { LoanApplications } from './loanapplications.model';

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
}
