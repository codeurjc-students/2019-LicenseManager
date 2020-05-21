import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import {MatSnackBar, MatDialogRef, MatDialog} from '@angular/material';
import { LoginService, User } from './login.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit  {
  @ViewChild('loginDialog',{static:false}) loginDialog: TemplateRef<any>;
  dialogRef: MatDialogRef<any, any>;

  user:User;
  error:boolean;
  constructor(public dialog: MatDialog,public snackBar: MatSnackBar, public loginService:LoginService, private router:Router) { 

  }
  ngOnInit(): void {
    this.user=this.loginService.getUserLogged();
    this.error=false;
  }

  getUserLogged(){
    return this.loginService.getUserLogged();
  }

  openLoginDialog() {
    this.error=false;
    this.dialogRef = this.dialog.open(this.loginDialog, {
        width: '50%',
        height: '50%',
    });
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }

  logIn(event: any, user: string, pass: string) {
    event.preventDefault();

    this.loginService.logIn(user, pass).subscribe(
        (u) => {
            this.dialogRef.close(); this.error=false;;
        },
        (error) => this.error=true,
    );
  }

  logOut() {
      this.loginService.logOut().subscribe(
          (response) => {
              this.router.navigate(['/products']);
          },
          (error) => console.log('Error when trying to log out: ' + error),
      );
  }

  register(){
    this.dialogRef.close();
    this.router.navigate(['/register']);
  }


  manageLinks(route:string){
    switch(route){
        case 'adminDashboard':{
            this.router.navigate(["/admin/dashboard"]);
            break;
        }
        case 'userDashboard':{
            this.router.navigate(["user/dashboard"]);
            break;
        }
        case 'userProfile':{
            this.router.navigate(["user/profile"]);
            break;
        }
        
    }
  }

  getUserLoggedBack(){
    this.loginService.getUserLoggedBack().subscribe(
      e=> console.log(e)
    )
  }
}
