
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatSnackBar, MatDialogRef, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { Observable, of } from 'rxjs';
import { DOMHelper } from 'src/testing/dom-helper';
import { User } from 'src/app/login/login.service';
import { UserProfileService } from '../../userProfile/userProfile.service';
import { CardSubscriptionSelectDialog } from './card-subscription-select-dialog.component';

describe('CardSubscriptionSelectDialog', () => {
  let component: CardSubscriptionSelectDialog;
  let fixture: ComponentFixture<CardSubscriptionSelectDialog>;
  let routerMock:any;
  let user:User={name:"Kike",roles:[],authdata:"",userStripeId:"stripe_id",email:"email@email.com"};
  let userProfileServiceMock:any;
  let domHelper:DOMHelper<CardSubscriptionSelectDialog>;
  let dialogRefMock:any;
  let paymentMethods:any[] = [];

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


    paymentMethods.push({id:1,card: {brand:"visa", last4:"4444"}});
    paymentMethods.push({id:2, card:{brand:"visa", last4:"5555"}});

    userProfileServiceMock = jasmine.createSpyObj("UserProfileService", ["getUserCardsStripe","getPaymentMethodOfSubs"]);
    userProfileServiceMock.getUserCardsStripe.and.returnValue(of(paymentMethods));
    userProfileServiceMock.getPaymentMethodOfSubs.and.returnValue(of({response:"1"}));




    TestBed.configureTestingModule({
      declarations: [ CardSubscriptionSelectDialog ],
      imports: [ MatDialogModule],

      schemas:[NO_ERRORS_SCHEMA],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: UserProfileService, useValue: userProfileServiceMock},
      //  { provide: MatDialogRef, useValue: {} },
        {provide:MatDialogRef, useClass:MatDialogRefMock},
        {provide:MatDialog, useClass:MatDialogMock},
        {provide:MatSnackBar},
        { provide: Router,useValue: routerMock},


      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardSubscriptionSelectDialog);
    component = fixture.componentInstance;
    domHelper = new DOMHelper(fixture);
  });


  describe("Page creation-display", () => {
      it("should create", () => {
          fixture.detectChanges();
      })
  })

  describe("Error handling", () => {
      it("should fail when getting userCardsStripe", () => {
        let error = new Error();
        userProfileServiceMock.getUserCardsStripe.and.returnValue(Observable.create(observer => {observer.error(error)}));
        console.log = jasmine.createSpy("log");
  
        fixture.detectChanges();

        expect(console.log).toHaveBeenCalledWith(error);

      })
  })

  describe("Methods", () => {

    it("should execute cardAddedHandler", () => {
        spyOn(component,"getCards");
        fixture.detectChanges();
        expect(component.getCards).toHaveBeenCalledTimes(1);
        component.cardAddedHandler(true);
        expect(component.getCards).toHaveBeenCalledTimes(2);
    })

    it("should call insertCard", () => {
        fixture.detectChanges();
        domHelper.clickButtonById("#insertCard-button");
        expect(component.inserted).toBe(true);
    })

    it("should call yes()", () => {
        fixture.detectChanges();
        expect(component.res[0]).toBe(undefined);
        domHelper.clickButtonById("#yes-button");
        expect(component.res[0]).toBe(true);
    })

    it("should call closeDialog()", () => {
        fixture.detectChanges();
        expect(component.res[0]).toBe(undefined);
        domHelper.clickButtonById("#close-icon");
        expect(component.res[0]).toBe(false);
    })

});

  
});
