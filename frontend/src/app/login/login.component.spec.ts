/*
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { LoginService, User } from './login.service';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { Observable } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    class LoginServiceStub {
      getUserLogged(){
        let user:User={name:"Kike",roles:[],authdata:"",userStripeId:"stripe_id",email:"email@email.com"};
        return user;
      }

      logIn(user: string, pass: string) {
        let userObj:User={name:"Kike",roles:[],authdata:"",userStripeId:"stripe_id",email:"email@email.com"};

        let resp =  Observable.of(userObj);
        return resp;
      }
    }
    const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

    TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      schemas:[NO_ERRORS_SCHEMA],
      providers: [
        {provide:MatDialog},
        {provide:MatSnackBar},
        {provide:LoginService, useClass:LoginServiceStub},
        { provide: Router,useValue: routerSpy},


      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should be logged Kike', () => {
    expect(component.user.name).toBe("Kike");
  })

  it('should login now', ()=>{
    let m :MouseEvent = new MouseEvent("click");
    
    component.logIn(m,"Kike","pass");

    expect(component.error).toBe(false);
  })


});
*/