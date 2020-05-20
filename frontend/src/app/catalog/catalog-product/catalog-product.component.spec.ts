import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule, ControlContainer } from '@angular/forms';

import { CatalogProductComponent } from './catalog-product.component';
import { MatGridListModule, MatIconModule, MatSelectModule, MatOptionModule, MatCardModule, MatSnackBarModule, MatDialogModule, MatDialog } from '@angular/material';
import { NgxPaginationModule} from 'ngx-pagination';
import { StripeService } from 'ngx-stripe';
import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { CardFormComponent } from 'src/app/userProfile/card-form/card-form.component';
import { AppService } from '../../app.service';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute, convertToParamMap } from '@angular/router';
import { LicenseService } from '../../licenses/license.service';
import { ProductService } from '../../product/product.service';
import { LoginService, User } from '../../login/login.service';
import { UserProfileService } from '../../userProfile/userProfile.service';
import { ActivatedRouteMock } from '../../mock/activated-route-mock.component';
import { of, Observable } from 'rxjs';
import { Product } from '../../product/product.model';
import { By } from '@angular/platform-browser';
import {DOMHelper} from '../../../testing/dom-helper'
import { License } from '../../licenses/license.model';
import { DialogService } from '../../dialogs/dialog.service';
import { UsedCardService } from '../../usedCard/usedCard.service';
import { IterableDiffers } from '@angular/core';

describe('CatalogProductComponent', () => {
  let component: CatalogProductComponent;
  let fixture: ComponentFixture<CatalogProductComponent>;
  let domHelper: DOMHelper<CatalogProductComponent>;

  //MOCKS
  let licenseServiceMock:any;
  let appServiceMock:any;
  let loginServiceMock: any;
  let dialogServiceMock:any;
  let userProfileServiceMock:any;
  let productServiceMock:any;
  let usedCardServMock:any;
  let routerMock:any;

  beforeEach(async(() => {

    //MOCKS
    let user:User={name:"Kike",roles:[],authdata:"",userStripeId:"stripe_id",email:"email@email.com"};
    let plansPrices:{[name:string]:number} = {["M"]:2, ["D"]:0.5};
    let prod:Product = {name: "Prod1", licenses:[], typeSubs:["M","D"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices,sku:null, active:true, trialDays:9, mode:"Both"};

    let lic:License = {serial:"serial", active:true, type:"M", product:prod, startDate:new Date(), endDate:new Date(), owner:"Kike", cancelAtEnd:false, subscriptionId:"sub_id", nUsage:1, price:2,trial:false, licenseStats:[],licenseString:"licFileStr",period:1}
    let card:any;


    routerMock = jasmine.createSpyObj('Router', ['navigate']);


    usedCardServMock = jasmine.createSpyObj("UsedCardService",["postUsedCard"]);
    usedCardServMock.postUsedCard.and.returnValue(of(card));

    licenseServiceMock = jasmine.createSpyObj('LicenseService',['getLicensesOfUserAndProduct']);
    let arrayLic:License[] = [];
    arrayLic.push(lic);
    licenseServiceMock.getLicensesOfUserAndProduct.and.returnValue(of(arrayLic));

    loginServiceMock= jasmine.createSpyObj("LoginService", ["getUserLogged"]);
    loginServiceMock.getUserLogged.and.returnValue(of(user));
    loginServiceMock.isLogged=true;

    dialogServiceMock = jasmine.createSpyObj("DialogService",["openFreeTrialDialog","openConfirmDialog","openCardSelectDialog","openAddCardDialog"]);
    let res:any[] = [];
    res[0]=true;
    res[1]=false;
    res[2]="pmId";
    dialogServiceMock.openConfirmDialog.and.returnValue({afterClosed: () => of(res)});
    dialogServiceMock.openCardSelectDialog.and.returnValue({afterClosed: () => of(res)});

    userProfileServiceMock = jasmine.createSpyObj("UserProfileService",["addFreeTrial","addSubscriptionToProduct","check3dsPayment","check3dsSubs"]);
    userProfileServiceMock.addSubscriptionToProduct.and.returnValue(of(lic))

    productServiceMock = jasmine.createSpyObj("ProductService", ["getProduct"]);
    productServiceMock.getProduct.and.returnValue(of(prod));


    TestBed.configureTestingModule({
      declarations: [ CatalogProductComponent ,CardFormComponent ],
      imports: [ ReactiveFormsModule,MatGridListModule,MatIconModule,MatCardModule, MatSelectModule,MatOptionModule,NgxPaginationModule, MatSnackBarModule,HttpClientModule, FormsModule ],
      providers: [
        { provide: StripeService },
        { provide: CardFormComponent },
        { provide: AppService },
        { provide: DatePipe },
        { provide: Router,useValue: routerMock},
        { provide: LicenseService , useValue: licenseServiceMock},
        { provide: MatDialog },
        { provide: ActivatedRoute },
        { provide: ProductService, useValue:productServiceMock },
        { provide: LoginService, useValue:loginServiceMock },
        { provide:UsedCardService, useValue: usedCardServMock },
        { provide: UserProfileService, useValue:userProfileServiceMock },
        {
          provide: ActivatedRoute,
          useClass: ActivatedRouteMock
        },
        { provide:DialogService, useValue:dialogServiceMock}
      ]

    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CatalogProductComponent);
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

  
    it('should be placed the product info on screen', () => {
      fixture.detectChanges();

        let name = domHelper.getText('#productName');
        let webLink = domHelper.getText('#webLink');
        let photo = domHelper.getObject('#photo');
        let description = domHelper.getText('#description');
        let freeTrial = domHelper.getText('#freeTrialComp');
  
        expect(component.product.name).toContain(name);
        expect(webLink).toContain(component.product.webLink);
        expect(photo).toBeNull();
        expect(description).toContain(component.product.description);
        expect(freeTrial).toContain(component.product.trialDays);
        Object.entries(component.product.plansPrices).forEach(
          value => {
            let id = "#plan"+value[0];
            expect(domHelper.getObject(id)).toBeTruthy();
          }
        )
    })

    it('should init with recent Lifetime buy with 3ds valid', () => {
      localStorage.setItem("pi","pi_d");
      let plansPrices:{[name:string]:number} = {["L"]:5};

      let prod2:Product = {name: "ProdL", licenses:[], typeSubs:["L"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices,sku:null, active:true, trialDays:9, mode:"Both"};

      let lic:License = {serial:"serial", active:true, type:"L", product:prod2, startDate:new Date(), endDate:new Date(), owner:"Kike", cancelAtEnd:false, subscriptionId:"sub_id", nUsage:1, price:5,trial:false, licenseStats:[],licenseString:"licFileStr",period:1}

      userProfileServiceMock.check3dsPayment.and.returnValue(of(lic));
      fixture.detectChanges();
      expect(component.serial).toContain(lic.serial);
    })

    it('should init with recent Lifetime buy with 3ds NOT valid', () => {
      localStorage.setItem("pi","pi_d");
      userProfileServiceMock.check3dsPayment.and.returnValue(Observable.create(observer => {observer.error(new Error())}));
      fixture.detectChanges();
      expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(1);
    })

    it('should init with recent subscription with 3ds valid', () => {
      localStorage.setItem("pi_s","pi_d");
      let plansPrices:{[name:string]:number} = {["M"]:5};

      let prod2:Product = {name: "ProdL", licenses:[], typeSubs:["M"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices,sku:null, active:true, trialDays:9, mode:"Both"};

      let lic:License = {serial:"serial", active:true, type:"M", product:prod2, startDate:new Date(), endDate:new Date(), owner:"Kike", cancelAtEnd:false, subscriptionId:"sub_id", nUsage:1, price:5,trial:false, licenseStats:[],licenseString:"licFileStr",period:1}

      userProfileServiceMock.check3dsSubs.and.returnValue(of(lic));
      fixture.detectChanges();
      expect(component.serial).toContain(lic.serial);

    })

    it('should init with recent subscription with 3ds NOT valid', () => {
      localStorage.setItem("pi_s","pi_d");
      userProfileServiceMock.check3dsSubs.and.returnValue(Observable.create(observer => {observer.error(new Error())}));
      fixture.detectChanges();
      expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(1);

    })

 })

  describe("Navigation",() => {
    
    it('should navigate to user/dashboard',()=> {
      fixture.detectChanges();

      component.manageLicenses();

      expect(routerMock.navigate).toHaveBeenCalledWith(['user/dashboard']);

    })

  })
  


  describe("Method Calls", () => {

    
    it('should call subscribeToProduct with the planName and planValue', async() => {

        fixture.detectChanges();
        spyOn(component, 'subscribeToProduct');
        domHelper.clickButtonById("#buttonSubsM");
        expect(component.subscribeToProduct).toHaveBeenCalledWith("M",2); 

    })

    it('should subscribe To Product',async()=> {
      fixture.detectChanges();

      let buttons = domHelper.findAllButtonsWithName('SUBSCRIBE');
      let typeSubs = buttons[0].id.charAt(buttons[0].id.length-1);
      let price = component.product.plansPrices[typeSubs];


      component.subscribeToProduct(typeSubs,price)

      expect(component.serial).toBe("serial");

      expect(component.successfulMessage).toBe(true);

    })


    it('should need subscription 3dsecure action', () => {

      let plansPrices:{[name:string]:number} = {["M"]:2, ["D"]:0.5};
      let prod2:Product = {name: "Prod2", licenses:[], typeSubs:["M","D"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices,sku:null, active:true, trialDays:9, mode:"Both"};
      let licReq:License = {serial:"serial", active:true, type:"RequiresAction", product:prod2, startDate:new Date(), endDate:new Date(), owner:"Kike", cancelAtEnd:false, subscriptionId:"sub_id", nUsage:1, price:2,trial:false, licenseStats:[],licenseString:"licFileStr",period:1}
      userProfileServiceMock.addSubscriptionToProduct.and.returnValue(of(licReq));
      let lic:License = {serial:"serial", active:true, type:"M", product:prod2, startDate:new Date(), endDate:new Date(), owner:"Kike", cancelAtEnd:false, subscriptionId:"sub_id", nUsage:1, price:2,trial:false, licenseStats:[],licenseString:"licFileStr",period:1}
      
      productServiceMock.getProduct.and.returnValue(of(prod2));

      spyOn(component, 'redirect');

      fixture.detectChanges();

      let buttons = domHelper.findAllButtonsWithName('SUBSCRIBE');
      let typeSubs = buttons[0].id.charAt(buttons[0].id.length-1);
      let price = component.product.plansPrices[typeSubs];

      component.subscribeToProduct(typeSubs,price);

      expect(component.redirect).toHaveBeenCalled();

      });

      it('should not be logged when trying to subscribe',()=>{
        loginServiceMock.getUserLogged.and.returnValue(null);
        fixture.detectChanges();
        component.subscribeToProduct("M",2);

        expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(1);

        component.freeTrial();
        expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(2);

        component.pay2(3);
        expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(3);



      })

      it('should not try to subscribe',()=>{

        let res:any[] = [];
        res[0]=false;
        dialogServiceMock.openCardSelectDialog.and.returnValue({afterClosed: () => of(res)});
        fixture.detectChanges();

        component.subscribeToProduct("MB",2);

        expect(component.loading).toBe(false);
        expect(component.successfulMessage).toBe(false);
         
      })

      it('should get a free trial',() => {
        let res:any[] = [];
        let params:any[] = [];
        params[0] = [];
        res[0]= params;
        res[1]="M";
        dialogServiceMock.openFreeTrialDialog.and.returnValue({afterClosed: () => of(res)});
        userProfileServiceMock.addFreeTrial.and.returnValue(of(true));


        fixture.detectChanges();

        expect(component.successfulMessage).toBe(false);

        component.freeTrial();

        expect(component.successfulMessage).toBe(true);
      })

      it('should copy the serial to the clipboard',()=> {
        spyOn(component.snackbar,"open");
        fixture.detectChanges();
        component.copyMessage("serial-to-copy");
        expect(component.snackbar.open).toHaveBeenCalledTimes(1);       
      })

      it('should get the path of a product photo', () => {
        fixture.detectChanges();
        let prod = domHelper.getText("#productName");
        let path = component.pathPhotos(prod);
        expect(path).toContain(prod+ "/image");
      })


      describe("ERROR TREATMENT", ()=> {
        it('should treat the error 428', ()=>{
          let error:HttpErrorResponse = new HttpErrorResponse({status:428});
  
          dialogServiceMock.openAddCardDialog.and.returnValue({afterClosed: () => of(true)});
          fixture.detectChanges();
  
          spyOn(component,"subscribeToProduct");
  
          expect(component.subscribeToProduct).toHaveBeenCalledTimes(0);
  
          
          component.treatmentBuyError(error,"M","2");
  
          expect(component.subscribeToProduct).toHaveBeenCalledTimes(1);
  
        })
  
        it('should treat the error 409', ()=>{
          let error:HttpErrorResponse = new HttpErrorResponse({status:409});
  
          fixture.detectChanges();
  
          expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(0);
  
          
          component.treatmentBuyError(error,"M","2");
  
          expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(1);
  
        })
  
        it('should treat random error', ()=>{
          let error:HttpErrorResponse = new HttpErrorResponse({status:488});
  
          fixture.detectChanges();
  
          expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(0);
  
          component.treatmentBuyError(error,"M","2");
  
          expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalledTimes(1);
  
        })

        it('should fail when getting a free trial',() => {
          let res:any[] = [];
          let params:any[] = [];
          params[0] = [];
          res[0]= params;
          res[1]="M";
  
          userProfileServiceMock.addFreeTrial.and.returnValue(Observable.create(observer => {observer.error(new Error())}));
          dialogServiceMock.openFreeTrialDialog.and.returnValue({afterClosed: () => of(res)});
          spyOn(component,"treatmentBuyError");
  
          fixture.detectChanges();
  
          expect(component.treatmentBuyError).toHaveBeenCalledTimes(0);
          component.freeTrial();
          expect(component.treatmentBuyError).toHaveBeenCalledTimes(1);
        })
  
  
        it('should fail when posting usedCard on a free trial',() => {
          let res:any[] = [];
          let params:any[] = [];
          params[0] = [];
          res[0]= params;
          res[1]="M";
          let error = new Error();
          usedCardServMock.postUsedCard.and.returnValue(Observable.create(observer => {observer.error(error)}));
          dialogServiceMock.openFreeTrialDialog.and.returnValue({afterClosed: () => of(res)});
          userProfileServiceMock.addFreeTrial.and.returnValue(of(true));
          console.log = jasmine.createSpy("log");
  
          fixture.detectChanges();
  
          component.freeTrial();
  
  
          expect(console.log).toHaveBeenCalledWith(error);
  
        })
  
      })


      
        
    

    })


});
