import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthRequest } from '../authRequest.model';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm=new FormGroup({
    email:new FormControl(),
    password:new FormControl(),
  })

  authRequest = new AuthRequest();

  constructor(private _userSrv:UserService, private _router:Router){}

  resetLogin(){
    this.authRequest.email='';
    this.authRequest.password='';
  }

  login(){
    if(this.loginForm.valid){
      this._userSrv.login(this.authRequest).subscribe(
        data=>{
          localStorage.setItem('token', data);
          localStorage.setItem('email', this.authRequest.email);
          this.getCreditScore();
          console.log(data)
          this._router.navigate(['/home'])
          this.token=data;
  
        },
        error=>{
          // console.log(error)
          alert("Invalid username or password") 
          this.resetLogin();
        }
      )
    }
    else{
      alert('Enter email and password')
    }
    

  }

  token='';

  getRequestFromJWT(){
    //this.token = localStorage.getItem('token');
    this._userSrv.getRequestFromJWT(this.token).subscribe(
      data=>{
        console.log(data)
      },
      error=>{
        console.log(error)
      }
    )

  }

  logout(){
    console.log("Logging out!")
    localStorage.clear();
  }

   authRequest2 = new AuthRequest();
  getCreditScore(){

    this.authRequest2.email = localStorage.getItem('email')+'';
    this._userSrv.getUserById(this.authRequest2).subscribe(
      data=>{
        console.log(data)
        console.log('credit score: '+data.creditScore)
        localStorage.setItem('creditScore',data.creditScore.toString());
      },
      error=>{

        console.log(error)
      }
    )
  }

}
