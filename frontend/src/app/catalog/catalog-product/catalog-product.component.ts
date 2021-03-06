import { Component, OnInit } from '@angular/core';
import { Product } from '../../product/product.model';
import { ProductService } from '../../product/product.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { LoginService, User } from '../../login/login.service';
import { UserProfileService } from '../../userProfile/userProfile.service';
import { DialogService } from '../../dialogs/dialog.service';
import { LicenseService } from '../../licenses/license.service';
import { License } from '../../licenses/license.model';
import { DatePipe } from '@angular/common';
import { MatDialogRef, MatSnackBarConfig, MatSnackBar } from '@angular/material';
import { AppService } from '../../app.service';
import { CardFormComponent } from '../../userProfile/card-form/card-form.component';
import { UsedCardService } from '../../usedCard/usedCard.service';
import { StripeService, Elements, Element as StripeElement, ElementsOptions } from 'ngx-stripe';
import { FormGroup } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
const BASE_URL_PRODUCT = "/api/products/"


@Component({
  selector: 'app-catalog-product',
  templateUrl: './catalog-product.component.html',
  styleUrls: ['./catalog-product.component.css']
})
export class CatalogProductComponent implements OnInit {

  fileurl;
  product?:Product;
  user:User;
  userLicensesOfProduct:License[];
  errorAdded:boolean;
  card2:boolean;
  successfulMessage:boolean;
  serial:string;
  tk:string;
  loading:boolean;
  numberOfPlans:number;
  dialogRef: MatDialogRef<any, any>;
  pageActual:number = 1;
  numberOfElements = 3;
  licenseFileString:string="";


  elements: Elements;
  card: StripeElement;
  purchase:boolean;
  elementsOptions: ElementsOptions = {
    locale: 'en'
  };

  fileName:string;

  constructor(private sanitizer: DomSanitizer,public snackbar:MatSnackBar,private stripeService: StripeService, public usedCardServ:UsedCardService,public cardForm:CardFormComponent,public appService:AppService,private datepipe:DatePipe,private router:Router,private licenseServ:LicenseService,private dialogService:DialogService,private activeRoute: ActivatedRoute,public productService:ProductService, private loginService:LoginService, private userProfileService:UserProfileService) {
    
   }

   public stripeForm = new FormGroup({ });


   pathPhotos(productName:string){
    return BASE_URL_PRODUCT + productName +"/image";
  }



  ngOnInit() {
    this.loadStripe();
    this.user=this.loginService.getUserLogged();
    this.purchase=false;
    let productName;
    this.activeRoute.paramMap.subscribe((params: ParamMap) => {
        productName = params.get('name');
    });
    this.productService.getProduct(productName).subscribe(
      (prd:any) => {this.product = prd; 
        let s= Object.keys(this.product.plansPrices); 
        this.numberOfPlans=s.length; 
        if(this.loginService.isLogged){
          this.getLicensesOfProductAndUser();
          this.fileName= "license-"+this.product.name +"-"+this.loginService.getUserLogged().name+ ".txt"; 

          if(localStorage.getItem("product_bought")==this.product.name){ //Product redirected by bought of this same product

              //Checking if redirected by a Lifetime buy
              let piId = localStorage.getItem("pi");
              if(piId!=null){
                localStorage.removeItem("pi");
          
                this.userProfileService.check3dsPayment(this.user.name,this.product,piId).subscribe(
                  (t:any) => {this.successfulMessage=true; this.serial=t.serial;this.loading=false;this.purchase=false;this.licenseFileString=t.licenseString; this.createFile();document.getElementById("exp").scrollIntoView()},
                  error => {                      
                    this.dialogService.openConfirmDialog("The purchase has not been posible. Try again with other card",false,false); 
                    this.loading=false; 
                    this.purchase=false;
                  }
                )
              }

              //Checking if redirected by a Subscription buy
              let piId_s = localStorage.getItem("pi_s");
              if(piId_s!=null){
                localStorage.removeItem("pi_s");
                let automaticRenewal;
                if(localStorage.getItem("automaticRenewal")=="true"){
                  automaticRenewal=true;
                }else{
                  automaticRenewal = false;
                }
                this.userProfileService.check3dsSubs(this.user.name,this.product,piId_s, automaticRenewal,localStorage.getItem("type"),localStorage.getItem("subscriptionId")).subscribe(
                  (t:any) => {this.successfulMessage=true; this.serial=t.serial;this.loading=false;this.purchase=false;this.licenseFileString=t.licenseString; this.createFile();document.getElementById("exp").scrollIntoView()},
                  error => {                      
                    this.dialogService.openConfirmDialog("The purchase has not been posible. Try again with other card",false,false); 
                    this.loading=false; 
                    this.purchase=false;
                  }
                )
              }

              localStorage.removeItem("type");
              localStorage.removeItem("automaticRenewal");
              localStorage.removeItem("subscriptionId");

          }


        }
        
    },
      error => console.log(error)
    );
    this.successfulMessage=false;
    this.loading=false;

  }




//METHODS TO SUBSCRIBE TO A PRODUCT
  subscribeToProduct(type:string,money:any){
    let msg;
    if(this.loginService.getUserLogged()==null){
     this.dialogService.openConfirmDialog("You have to be logged first! Click 'OK' to Log in or Sign up",false,false).afterClosed().subscribe(
      res => {   
        if(res[0]==true){
          let element: HTMLElement = document.getElementById('loginButton');
          element.click();
        }
      },
      error => console.log(error)
    );
  
    }else{
      if(type==="MB"){
        msg = "You are going to subscribe to " + this.product.name + " with a " + type + " subscription. You will be charged in 1 month with the sum of usages * " + money + "€ to the card selected:"
      }else{
        msg="You are going to subscribe to " + this.product.name + " with a " + type + " subscription. You will be charged now " + money + "€ to the card selected:";
      }
      this.dialogService.openCardSelectDialog(this.loginService.getUserLogged().name, msg,true).afterClosed().subscribe(
        res=>{
          if(res[0]){
            this.loading=true;
            this.userProfileService.addSubscriptionToProduct(this.product,type,this.loginService.getUserLogged().name, res[1], res[2]).subscribe(
                (u:any)=> { 
                  if(u.type!="RequiresAction"){
                    this.successfulMessage=true;
                    this.loading=false;
                    this.serial=u.serial; 
                    this.licenseFileString=u.licenseString; 
                    this.createFile();
                  }else{
                    localStorage.setItem("product_bought",this.product.name);
                    localStorage.setItem("pi_s",u.owner);
                    localStorage.setItem("type",type);
                    localStorage.setItem("automaticRenewal",res[1]);
                    localStorage.setItem("subscriptionId", u.subscriptionId);
                    this.redirect(u.serial);
                  }
                },
                error=> {this.treatmentBuyError(error, type,money);this.loading=false;},
            )
          }else{
            this.loading=false;
          }
        }
      )
    }

  }

  redirect(location:any){
    window.location=location;
  }

  createFile(){
    const blob = new Blob([this.licenseFileString], { type: 'application/octet-stream' });
    this.fileurl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob));
  }


  treatmentBuyError(error:any,type:string,money:string){
      if(error.status === 428){
          this.dialogService.openAddCardDialog().afterClosed().subscribe(
            res=> {
                    if(res){
                      this.subscribeToProduct(type,money);
                    }
            }
          );
      }else if (error.status === 409){
          this.dialogService.openConfirmDialog("You already have a subscription of this type to this product!",false,false)
      }else{
        this.dialogService.openConfirmDialog("There has been an error with the subscription and it couldn't be successful.",false,false)
      }
  }


  freeTrial(){
    if(this.loginService.getUserLogged()==null){
      this.dialogService.openConfirmDialog("You have to be logged first! Click 'OK' to Log in or Sign up",false,false).afterClosed().subscribe(
       res => {    
         if(res[0]==true){
            let element: HTMLElement = document.getElementById('loginButton');
            element.click();
         }
       },
       error => console.log(error)
     );
   
     }else{
      this.dialogService.openFreeTrialDialog(this.product.name, this.product.plansPrices).afterClosed().subscribe(
        res=> {
          if (res!=null){
            this.loading=true;
            this.userProfileService.addFreeTrial(this.product,this.loginService.getUserLogged().name,this.product.trialDays, res[0][0], res[1]).subscribe(
                (u:any)=> {
                  this.usedCardServ.postUsedCard(res[0][1], res[0][2], res[0][3],this.product.name).subscribe(
                    card => {this.loading=false;},
                    error=>{this.loading=false;console.log(error);}
                  )
                  this.successfulMessage=true;this.loading=false;this.serial=u.serial;this.licenseFileString=u.licenseString; this.createFile()},
                error=> {this.treatmentBuyError(error,null,null);this.loading=false;},
            )
          }
        }
      )
    }
  }
  



  //Function to copy the license serial to the clipboard
  copyMessage(val: string){
    const selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = val;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
    let conf:MatSnackBarConfig= new MatSnackBarConfig();
    conf.duration=3000;
    this.snackbar.open('Copied!',"X",conf);
  }

  getLicensesOfProductAndUser(){
    this.licenseServ.getLicensesOfUserAndProduct(this.loginService.getUserLogged().name,this.product.name).subscribe(
      (ls:any)=> {this.userLicensesOfProduct=ls},
      error => console.log(error)
    );
  }

  manageLicenses(){
    this.router.navigate(["user/dashboard"]);
  }





  //METHODS TO CHECKOUT A SIMPLE-PAY PRODUCT
  loadStripe() {	 
    if(!window.document.getElementById('stripe-script')) {	    
        var s = window.document.createElement("script");	  
        s.id = "stripe-script";	
        s.type = "text/javascript";	
        s.src = "https://checkout.stripe.com/checkout.js";	
        window.document.body.appendChild(s);	
    }	
    
  }

  pay2(amount) {  
    let user=this.loginService.getUserLogged();
    if(this.loginService.getUserLogged()==null){
      this.dialogService.openConfirmDialog("You have to be logged first! Click 'OK' to Log in or Sign up",false,false).afterClosed().subscribe(
       res => {    
         if(res[0]==true){
            let element: HTMLElement = document.getElementById('loginButton');
            element.click();
         }

       },
       error => console.log(error)
     );
      
    }else{
      var handler = (<any>window).StripeCheckout.configure({
        key: this.appService.publicApiKey,
        currency:'eur',
        locale:'auto',
        email:this.user.email,
        token: (token: any) =>{
          if(token!=null){
            this.loading=true;
            this.userProfileService.pay(user.name,this.product, token.id).subscribe(
              data => {
                this.userProfileService.confirmPay(user.name,this.product,data[`id`]).subscribe(
                  (t:any) => {
                    if(t.type != "RequiresAction"){
                      this.successfulMessage=true; 
                      this.serial=t.serial;
                      this.loading=false;
                      this.purchase=false;
                      this.licenseFileString=t.licenseString; 
                      this.createFile()
                    }else{
                      localStorage.setItem("pi",data['id']);
                      localStorage.setItem("product_bought",this.product.name);

                      console.log(t.serial);
                      window.location=t.serial;
                    }
                  },
                  (error:any) => {
                    console.log(error.body);
                    if(error.status == 304){
                      console.log(error)
                     
                    }else{
                      this.dialogService.openConfirmDialog("The purchase has not been posible",false,false); 
                      this.loading=false; 
                      this.purchase=false;
                    }
                  }
                )
              }
            );
          }
        }
      });
  
      handler.open({
        name: this.product.name, 
        description: 'This license is valid forever',
        amount: amount * 100
      });

    }

    
  
  }

}
 