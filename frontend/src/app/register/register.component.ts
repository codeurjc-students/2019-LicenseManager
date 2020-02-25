import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from '@angular/material';
import { RegisterService } from './register.service';
import { LoginService } from '../login/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {


  emailLinked:string;
  loading:boolean;

  constructor(private router:Router,private snackBar: MatSnackBar, private registerService:RegisterService, private loginService:LoginService) { 
    
  }
  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }

  ngOnInit() {
  }

  register(userName:string,pass1:string,pass2:string,email:string){
    if (userName=='' || pass1=='' || pass2=='' || email==''){
      alert("One o more fields were not introduced")
    }else{
      if(!this.emailLinked.match('[a-zA-Z0-9.-_]*@[a-zA-Z.-]*.[a-zA-Z]*')){
        alert("Introduce valid email")
      }else{
        this.loading=true;
        this.registerService.register(userName,pass1,pass2,email).subscribe(
            user=>{this.loading=false; this.loginService.logIn(userName,pass1).subscribe(
                    log=>  this.router.navigate(["/"]))},
            (error)=>{this.treatmentError(error);this.loading=false;},
        ) 
      }
    }
  }

  treatmentError(error:any){
    if(error.status === 409){
      alert("Email or Username already exist")
    }
  }
}
