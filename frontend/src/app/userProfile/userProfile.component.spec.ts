import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MatGridListModule, MatIconModule, MatSelectModule, MatOptionModule, MatCardModule, MatDialog } from '@angular/material';

import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';

import { of, Observable } from 'rxjs';

import { DOMHelper } from 'src/testing/dom-helper';
import { ActivatedRouteMock } from '../mock/activated-route-mock.component';
import { User, LoginService } from '../login/login.service';
import { UserProfileService } from '../userProfile/userProfile.service';
import { DialogService } from '../dialogs/dialog.service';
import { UserProfileComponent } from './userProfile.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('UserProfileComponent', () => {
  let component: UserProfileComponent;
  let fixture: ComponentFixture<UserProfileComponent>;
  let domHelper: DOMHelper<UserProfileComponent>;

  //MOCKS
  let loginServiceMock: any;
  let dialogServiceMock:any;
  let userProfileServiceMock:any;
  let routerMock:any;

  let user:User;

  let paymentMethods:any[]=[]

  beforeEach(async(() => {

    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    //MOCKS
    user={name:"Kike",roles:[],authdata:"",userStripeId:"stripe_id",email:"email@email.com"};


    loginServiceMock= jasmine.createSpyObj("LoginService", ["getUserLogged"]);
    loginServiceMock.getUserLogged.and.returnValue(of(user));
    loginServiceMock.isLogged=true;
    loginServiceMock.user = user;

    dialogServiceMock = jasmine.createSpyObj("DialogService",["openConfirmDialog","openCardSelectDialog"]);

    
    let res:any[] = [];

    res[0]=true;
    res[1]=false;
    res[2]="pmId";
    dialogServiceMock.openConfirmDialog.and.returnValue({afterClosed: () => of(res)});

    paymentMethods.push({id:1,card: {brand:"visa", last4:"4444"}});
    paymentMethods.push({id:2, card:{brand:"visa", last4:"4444"}});

    userProfileServiceMock = jasmine.createSpyObj("UserProfileService",["setDefaultCard","deleteStripeCard","getUserCardsStripe","getDefaultCard"]);
    userProfileServiceMock.getUserCardsStripe.and.returnValue(of(paymentMethods))
    userProfileServiceMock.getDefaultCard.and.returnValue(of("response"));
    userProfileServiceMock.deleteStripeCard.and.returnValue(of(true));
    userProfileServiceMock.setDefaultCard.and.returnValue(of(true));

    TestBed.configureTestingModule({
      declarations: [ UserProfileComponent  ],
      schemas:[NO_ERRORS_SCHEMA],

      imports: [ MatCardModule, MatGridListModule, NgxPaginationModule, MatOptionModule, MatSelectModule, MatIconModule, MatCardModule],
      providers: [
          { provide: DatePipe,provider:DatePipe},
          { provide: UserProfileService, useValue: userProfileServiceMock},
          { provide: LoginService, useValue:loginServiceMock},
          { provide: MatDialog, provider: MatDialog},
          { provide: Router,useValue: routerMock},
          {provide: DialogService, useValue:dialogServiceMock},
          {
            provide: ActivatedRoute,
            useClass: ActivatedRouteMock
          },
      ]

    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserProfileComponent);
    component = fixture.componentInstance;
    domHelper = new DOMHelper(fixture);
  });

  afterEach( () => {
    fixture.destroy();
  })
      

  
  describe("Page creation-display",()=> {

    it('should create', async() => {
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });

 })

 describe("Error handling", () => {
     it("should fail when getting userCardsStripe", () => {
        let error = new Error();
        userProfileServiceMock.getUserCardsStripe.and.returnValue(Observable.create(observer => {observer.error(error)}));
        console.log = jasmine.createSpy("log");
  
        fixture.detectChanges();

        expect(console.log).toHaveBeenCalledWith(error);
     })

     it("should fail when getting the default card", () => {
        let error = new Error();
        userProfileServiceMock.getDefaultCard.and.returnValue(Observable.create(observer => {observer.error(error)}));
        console.log = jasmine.createSpy("log");
  
        fixture.detectChanges();

        expect(console.log).toHaveBeenCalledWith(error);
     })

     it("should fail when deleting a card", () => {
        let error = new Error();
        userProfileServiceMock.deleteStripeCard.and.returnValue(Observable.create(observer => {observer.error(error)}));
        console.log = jasmine.createSpy("log");
  
        fixture.detectChanges();
        component.deleteCard("any");

        expect(console.log).toHaveBeenCalledWith(error);
     })

     it("should fail when setting default a card", () => {
        let error = new Error();
        userProfileServiceMock.setDefaultCard.and.returnValue(Observable.create(observer => {observer.error(error)}));
        console.log = jasmine.createSpy("log");
  
        fixture.detectChanges();
        component.setDefault("any");

        expect(console.log).toHaveBeenCalledWith(error);
     })
 })

 describe("Method calls", () => {
    it("should delete when clicked Trash Icon", () => {
        fixture.detectChanges();
        spyOn(component,"getCards");

        domHelper.clickButtonById("#deleteButton-"+paymentMethods[0].id);
        expect(component.getCards).toHaveBeenCalled();
        
    })

    it("should set default card when clicked Set Default Icon", () => {
        fixture.detectChanges();
        spyOn(component,"getCards");
        domHelper.clickButtonById("#setDefault-"+paymentMethods[0].id);
        expect(component.getCards).toHaveBeenCalled();
        
    })

    it("should execute updateURL and check errors", () => {
        fixture.detectChanges();
        expect(component.amexError).toBe(false);
        expect(component.visaError).toBe(false);
        expect(component.mcError).toBe(false);

        component.updateUrl("visa");
        expect(component.visaError).toBe(true);

        component.updateUrl("mastercard");
        expect(component.mcError).toBe(true);

        component.updateUrl("amex");
        expect(component.amexError).toBe(true);

    })

   

 })



});
