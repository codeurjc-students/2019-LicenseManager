
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatSnackBar, MatDialogRef } from '@angular/material';
import { LoginService, User } from './login.service';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { Observable, of } from 'rxjs';
import { DOMHelper } from '../../testing/dom-helper';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let loginServiceMock:any;
  let routerMock:any;
  let user:User={name:"Kike",roles:[],authdata:"",userStripeId:"stripe_id",email:"email@email.com"};

  let domHelper:DOMHelper<LoginComponent>;
  let dialogRefMock:any;
  afterEach(()=>{
    fixture.destroy();
  })
  
  
  beforeEach(async(() => {

    class MatDialogRefMock {
      close(value = '') {
  
      }
    }

    class MatDialogMock {
      open() {
          return {
              afterClosed: () => of({ name: 'some object' })
          };
      }

      close(){

      }
    }

    dialogRefMock = jasmine.createSpyObj("DialogRef",["open","close"]);

    dialogRefMock.open.and.returnValue(true);
    dialogRefMock.close.and.returnValue(true);


    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    loginServiceMock = jasmine.createSpyObj("LoginService", ["getUserLogged", "logIn","getUserLoggedBack"]);
    loginServiceMock.getUserLogged.and.returnValue(of(user));
    loginServiceMock.getUserLoggedBack.and.returnValue(of(user));

    loginServiceMock.logIn.and.returnValue(of(user));


    TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      schemas:[NO_ERRORS_SCHEMA],
      providers: [
        {provide:MatDialogRef, useClass:MatDialogRefMock},
        {provide:MatDialog, useClass:MatDialogMock},
        {provide:MatSnackBar},
        {provide:LoginService, useValue:loginServiceMock},
        { provide: Router,useValue: routerMock},


      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    component.user=user;
    domHelper = new DOMHelper(fixture);
  });

  describe("Page creation-display", () => {

    it('should create', () => {
      fixture.detectChanges();

      expect(component).toBeTruthy();
    });

    it("should get user logged", () => {
      fixture.detectChanges();
      let userLog:any = component.getUserLogged();
      component.getUserLoggedBack();

      expect(userLog.value).toBe(user);

    })

  })

  describe("Methods", () => {

    it("should navigate", ()=> {
      fixture.detectChanges();
      component.manageLinks("adminDashboard");
      expect(routerMock.navigate).toHaveBeenCalledWith(["/admin/dashboard"]);
      component.manageLinks("userDashboard");
      expect(routerMock.navigate).toHaveBeenCalledWith(["user/dashboard"]);
      component.manageLinks("userProfile");
      expect(routerMock.navigate).toHaveBeenCalledWith(["user/profile"]);
    })

  })

});
