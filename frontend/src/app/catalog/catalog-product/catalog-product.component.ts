import { Component, OnInit, Input, OnChanges, SimpleChanges, Renderer, ViewChild, TemplateRef } from '@angular/core';
import { Product } from '../../product/product.model';
import { ProductService } from '../../product/product.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { LoginService, User } from '../../login/login.service';
import { UserProfileService } from '../../userProfile/userProfile.service';
import { DialogService } from '../../dialogs/dialog.service';
import { LicenseService } from '../../licenses/license.service';
import { License } from '../../licenses/license.model';
import { DatePipe } from '@angular/common';
import { MatDialogRef, MatDialog, MatSnackBarConfig, MatSnackBar } from '@angular/material';
import { LoginComponent } from '../../login/login.component';
import { AppService } from '../../app.service';
import { CardFormComponent } from '../../userProfile/card-form/card-form.component';
import { UsedCardService } from '../../usedCard/usedCard.service';
import { StripeService, Elements, Element as StripeElement, ElementsOptions } from 'ngx-stripe';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
const BASE_URL_PRODUCT = "https://localhost:8443/api/products/"


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

  constructor(private sanitizer: DomSanitizer,private snackbar:MatSnackBar,private stripeService: StripeService, public usedCardServ:UsedCardService,public cardForm:CardFormComponent,public appService:AppService,private datepipe:DatePipe,private router:Router,private licenseServ:LicenseService,private dialogService:DialogService,private activeRoute: ActivatedRoute,private productService:ProductService, private loginService:LoginService, private userProfileService:UserProfileService) {
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
        this.fileName= "license-"+this.product.name + ".txt"; }},
      error => console.log(error)
    );
    this.successfulMessage=false;
    this.loading=false;
   }

   public stripeForm = new FormGroup({ });


   pathPhotos(productName:string){
    return BASE_URL_PRODUCT + productName +"/image";
  }
  setUp(){
    this.appService.getPublicStripeKey().subscribe(
      (key:any)=> {
        this.stripeService.changeKey(key.text);
        this.stripeService.elements(this.elementsOptions).subscribe(
         elements => {
           this.elements = elements;
           if (!this.card) {
             this.card = this.elements.create('card', {
               style: {
                 base: {
                   iconColor: '#666EE8',
                   color: '#31325F',
                   fontWeight: 300,
                   fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
                   fontSize: '18px',
                   '::placeholder': {
                     color: '#3F51B5'
                   }
                 }
               }
             });
             this.card.mount('#card-element');
           }
         }
       );
      }
    );
  }


  ngOnInit() {
   this.user=this.loginService.getUserLogged();
  }




//METHODS TO SUBSCRIBE TO A PRODUCT
  subscribeToProduct(type:string,money:string){
    let msg;
    if(this.user==null){
     this.dialogService.openConfirmDialog("You have to be logged first! If you don't have an account, you can register too",false,false);
  
    }else{
      if(type==="MB"){
        msg = "You are going to subscribe to " + this.product.name + " with a " + type + " subscription. You will be charged in 1 month with the sum of usages * " + money + "€ to your default card."
      }else{
        msg="You are going to subscribe to " + this.product.name + " with a " + type + " subscription. You will be charged now " + money + "€ to your default card.";
      }
      this.dialogService.openConfirmDialog(msg,true,true)
      .afterClosed().subscribe(
        res=>{
          if(res[0]){
            this.loading=true;
            this.userProfileService.addSubscriptionToProduct(this.product,type, this.user.name, res[1]).subscribe(
                (u:any)=> {this.successfulMessage=true;this.loading=false;this.serial=u.serial; this.licenseFileString=u.licenseString; console.log(u.licenseString);this.createFile()},
                error=> {this.treatmentBuyError(error, type,money);this.loading=false;},
            )
          }
        }
      )
    }

  }

  createFile(){
    const blob = new Blob([this.licenseFileString], { type: 'application/octet-stream' });
    this.fileurl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob));
  }


  treatmentBuyError(error:any,type:string,money:string){
      if(error.status === 428){
          this.dialogService.openAddCardDialog().afterClosed().subscribe(
            res=> {console.log(res);
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
    this.dialogService.openFreeTrialDialog(this.product.name).afterClosed().subscribe(
      res=> {
        if (res!=null){
          this.loading=true;
          this.userProfileService.addFreeTrial(this.product,this.user.name,this.product.trialDays, res[0]).subscribe(
              (u:any)=> {
                this.usedCardServ.postUsedCard(res[1], res[2], res[3],this.product.name).subscribe(
                  card => {this.loading=false;},
                  error=>{this.loading=false;console.log(error);}
                )
                this.successfulMessage=true;this.loading=false;this.serial=u.serial;this.licenseFileString=u.licenseString; this.createFile()},
              error=> {this.treatmentBuyError(error,null,null);this.loading=false;},
          )
        }else{

        }
      }
    )

  }
  



//METHODS TO CHECKOUT A SIMPLE-PAY PRODUCT
  startPurchase(){
    this.purchase=true;
    this.setUp();
  }

  pay(){
    const name = this.user.name
    this.stripeService
      .createToken(this.card, { name })
      .subscribe(result => {
        if (result.token) {
          this.loading=true;
          this.userProfileService.pay(this.user.name,this.product, result.token.id).subscribe(
            data => {
              this.userProfileService.confirmPay(this.user.name,this.product,data[`id`]).subscribe(
                (t:any) => {this.successfulMessage=true; this.serial=t.serial;this.loading=false;this.purchase=false;this.licenseFileString=t.licenseString; this.createFile()},
                error => {this.dialogService.openConfirmDialog("The purchase has not been posible",false,false); console.log(error),this.loading=false; this.purchase=false;}
              )
            }
          );
          
        } else if (result.error) {
          
        }
      });
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
    this.licenseServ.getLicensesOfUserAndProduct(this.user.name,this.product.name).subscribe(
      (ls:any)=> {this.userLicensesOfProduct=ls.content},
      error => console.log(error)
    );
  }

  manageLicenses(){
    this.router.navigate(["user/dashboard"]);
  }

}
 