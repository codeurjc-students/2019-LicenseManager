import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MatGridListModule, MatIconModule, MatSelectModule, MatOptionModule, MatCardModule, MatSnackBarModule, MatDialogModule, MatDialog, MatSnackBar } from '@angular/material';
import { NgxPaginationModule} from 'ngx-pagination';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { of, Observable } from 'rxjs';
import { UserDashboardComponent } from './userDashboard.component';
import { DOMHelper } from 'src/testing/dom-helper';
import { ActivatedRouteMock } from '../mock/activated-route-mock.component';
import { LicenseService } from '../licenses/license.service';
import { License } from '../licenses/license.model';
import { Product } from '../product/product.model';
import { User, LoginService } from '../login/login.service';
import { UserProfileService } from '../userProfile/userProfile.service';
import { DialogService } from '../dialogs/dialog.service';

describe('UserDashboardComponent', () => {
  let component: UserDashboardComponent;
  let fixture: ComponentFixture<UserDashboardComponent>;
  let domHelper: DOMHelper<UserDashboardComponent>;

  //MOCKS
  let licenseServiceMock:any;
  let appServiceMock:any;
  let loginServiceMock: any;
  let dialogServiceMock:any;
  let userProfileServiceMock:any;
  let productServiceMock:any;
  let usedCardServMock:any;
  let routerMock:any;

  let user:User;
  let plansPrices:any;
  let plansPrices2:any;
  let plansPrices3:any;

  let prod:Product;
  let prod2:Product;
  let prod3:Product;

  let lic:License;
  let lic2:License;
  let lic3:License;

  beforeEach(async(() => {

    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    //MOCKS
    user={name:"Kike",roles:[],authdata:"",userStripeId:"stripe_id",email:"email@email.com"};
    plansPrices = {["M"]:2, ["D"]:0.5};
    plansPrices2 = {["L"]:2};
    plansPrices3 = {["MB"]:0.2};

    prod = {name: "Prod1", licenses:[], typeSubs:["M","D"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices,sku:null, active:true, trialDays:9, mode:"Both"};
    prod2 = {name: "Prod2", licenses:[], typeSubs:["L"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices2,sku:null, active:true, trialDays:9, mode:"Both"};
    prod3 = {name: "Prod3", licenses:[], typeSubs:["MB"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices3,sku:null, active:true, trialDays:9, mode:"Online"};

    lic = {id:1,serial:"serial", active:true, type:"M", product:prod, startDate:new Date(), endDate:new Date(), owner:"Kike", cancelAtEnd:false, subscriptionId:"sub_id", nUsage:1, price:2,trial:false, licenseStats:[],licenseString:"licFileStr",period:1}
    lic2 = {id:2,serial:"serial-2", active:true, type:"L", product:prod2, startDate:new Date(), endDate:new Date(), owner:"Kike", cancelAtEnd:true, subscriptionId:"sub_id", nUsage:1, price:2,trial:false, licenseStats:[],licenseString:"licFileStr",period:1}
    lic3 = {id:3,serial:"serial-3", active:true, type:"MB", product:prod3, startDate:new Date(), endDate:new Date(), owner:"Kike", cancelAtEnd:false, subscriptionId:"sub_id", nUsage:5, price:0.2,trial:false, licenseStats:[],licenseString:"licFileStr",period:1}

    let card:any;




    usedCardServMock = jasmine.createSpyObj("UsedCardService",["postUsedCard"]);
    usedCardServMock.postUsedCard.and.returnValue(of(card));

    licenseServiceMock = jasmine.createSpyObj('LicenseService',['getLicensesOfUser', "canceltAtEndLicense"]);
    let arrayLic:License[] = [];
    arrayLic.push(lic);
    arrayLic.push(lic2);
    arrayLic.push(lic3);
    licenseServiceMock.getLicensesOfUser.and.returnValue(of(arrayLic));
    let an:any;
    licenseServiceMock.canceltAtEndLicense.and.returnValue(of(an));

    loginServiceMock= jasmine.createSpyObj("LoginService", ["getUserLogged"]);
    loginServiceMock.getUserLogged.and.returnValue(of(user));
    loginServiceMock.isLogged=true;
    loginServiceMock.user = user;

    dialogServiceMock = jasmine.createSpyObj("DialogService",["openCardSubscriptionSelectDialog","openFreeTrialDialog","openConfirmDialog","openCardSelectDialog","openAddCardDialog"]);
    let res:any[] = [];
    res[0]=true;
    res[1]=false;
    res[2]="pmId";
    dialogServiceMock.openConfirmDialog.and.returnValue({afterClosed: () => of(res)});
    dialogServiceMock.openCardSelectDialog.and.returnValue({afterClosed: () => of(res)});
    dialogServiceMock.openCardSubscriptionSelectDialog.and.returnValue({afterClosed: () => of(res)});
    userProfileServiceMock = jasmine.createSpyObj("UserProfileService",["setPaymentMethodOfSubs"]);
    userProfileServiceMock.setPaymentMethodOfSubs.and.returnValue(of(true))

    productServiceMock = jasmine.createSpyObj("ProductService", ["getProduct"]);
    productServiceMock.getProduct.and.returnValue(of(prod));


    TestBed.configureTestingModule({
      declarations: [ UserDashboardComponent  ],
      imports: [ MatCardModule,MatGridListModule, MatIconModule, MatSelectModule, MatOptionModule, NgxPaginationModule ],
      providers: [
          { provide: DatePipe,provider:DatePipe},
          { provide: UserProfileService, useValue: userProfileServiceMock},
          { provide: LoginService, useValue:loginServiceMock},
          { provide:MatSnackBar, provider: MatSnackBarModule},
          { provide: MatDialog, provider: MatDialog},
          { provide:LicenseService, useValue: licenseServiceMock},
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
    fixture = TestBed.createComponent(UserDashboardComponent);
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

    it("should be displayed all the elements", () => {
        fixture.detectChanges();
        let numNotOnline=0;
        component.activeLicenses.forEach(license => {
            let productName = domHelper.checkExists("#productName"+license.product.name);
            expect(productName).toBe(true);
            if(license.product.mode!="Offline"){
                expect(domHelper.checkExists("#serial-" + license.id)).toBe(true);
            }

            if(license.active){
                expect(domHelper.checkExists("#serialActive-"+license.id));
            }else{
                expect(domHelper.checkExists("#serialInactive-"+license.id));
            }

            let type = domHelper.getText("#type-"+license.id);
            expect(type).toBe(license.type);

            if(license.type=="MB"){
                let inv = domHelper.getText("#invoice-"+license.id);
                expect(inv).toContain(license.nUsage*license.price);
            }

            if(license.product.mode!="Online"){
                numNotOnline++;
            }
        })

        expect(numNotOnline).toBe(domHelper.findAllButtonsWithName("Get LicenseFile").length);
    })

 })

  describe("Error handling", () => {
      it("should failt when getting the licenses", () => {
        let error = new Error();
        licenseServiceMock.getLicensesOfUser.and.returnValue(Observable.create(observer => {observer.error(error)}));
        console.log = jasmine.createSpy("log");
  
        fixture.detectChanges();

        expect(console.log).toHaveBeenCalledWith(error);

      }) 

      it("should fail when updating cancelAtEnd of a license", () => {
        let error = new Error();
        licenseServiceMock.canceltAtEndLicense.and.returnValue(Observable.create(observer => {observer.error(error)}));
        console.log = jasmine.createSpy("log");
  
        fixture.detectChanges();

        component.cancelAtEnd(lic);

        expect(console.log).toHaveBeenCalledWith(error);

      }) 

      it("should fail when setting paymentMethod to Subs", () => {
        let error = new Error();
        userProfileServiceMock.setPaymentMethodOfSubs.and.returnValue(Observable.create(observer => {observer.error(error)}));
        console.log = jasmine.createSpy("log");
  
        fixture.detectChanges();

        component.editPaymentMethod("mock");

        expect(console.log).toHaveBeenCalledWith(error);

      }) 
  })

  describe("Navigation", () => {

      it("should navigate to products/prodName", ()=>{
        fixture.detectChanges();
        let productName= "Prod1";
        component.manageLink(productName);
        expect(routerMock.navigate).toHaveBeenCalledWith(['products/',productName]);

      })

      it("should navigate to license stats", ()=> {
        fixture.detectChanges();
        let productName= "Prod1";
        let serial = "serial";
        component.goToStats(productName,serial);
        expect(routerMock.navigate).toHaveBeenCalledWith(["user/products/"+productName+"/statistics/"+serial]);
      })

  })

  describe("Method Calls", () => {

    it("should call cancelAtEnd", () => {
        spyOn(component,"cancelAtEnd")
        fixture.detectChanges();
        let clicks=0;
        component.activeLicenses.forEach(license=>{
            let obj;
            component.loading=false;
            if(license.cancelAtEnd){
                obj = domHelper.getObject("#enable-"+license.id);
                    
            }else{
                obj = domHelper.getObject("#disable-"+license.id);
            }    
   
            if(obj!=null){ //License type=L if obj=null
                const buttonElement: HTMLButtonElement =obj.nativeElement;
                buttonElement.click();  
                clicks++; 
            }
  
            expect(component.cancelAtEnd).toHaveBeenCalledTimes(clicks);
        })
    })

    it("should open dialog for changing cancelAtEnd of a license and call licenseService to update it", ()=>{
        fixture.detectChanges();

        component.cancelAtEnd(lic);
        component.cancelAtEnd(lic2);

        expect(licenseServiceMock.canceltAtEndLicense).toHaveBeenCalledTimes(2);
    })

    it("should open the download dialog", () => {
        fixture.detectChanges();

        component.downloadDialog(lic);

        expect(dialogServiceMock.openConfirmDialog).toHaveBeenCalled();
    })

    it("should open cardSubscriptionSelectDialog", () =>{
        fixture.detectChanges();

        component.editPaymentMethod("mock");

        expect(dialogServiceMock.openCardSubscriptionSelectDialog).toHaveBeenCalled();

        expect(userProfileServiceMock.setPaymentMethodOfSubs).toHaveBeenCalled();



    })



  })


});
