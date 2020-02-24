import { Component, OnInit, Input, OnChanges, SimpleChanges, Renderer, ViewChild, TemplateRef } from '@angular/core';
import { Product } from '../../product/product.model';
import { ProductService } from '../../product/product.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { LoginService, User } from '../../login/login.service';
import { UserProfileService } from '../../userProfile/userProfile.service';
import { DialogService } from 'src/app/dialogs/dialog.service';
import { LicenseService } from '../../licenses/license.service';
import { License } from 'src/app/licenses/license.model';
import { DatePipe } from '@angular/common';
import { MatDialogRef, MatDialog } from '@angular/material';
import { LoginComponent } from '../../login/login.component';
import { AppComponent } from 'src/app/app.component';
import { environment } from 'src/environments/environment';
import { AppService } from '../../app.service';


@Component({
  selector: 'app-catalog-product',
  templateUrl: './catalog-product.component.html',
  styleUrls: ['./catalog-product.component.css']
})
export class CatalogProductComponent implements OnInit {



  product?:Product;
  user:User;
  userLicensesOfProduct:License[];
  errorAdded:boolean;
  card:boolean;
  successfulMessage:boolean;
  serial:string;
  tk:string;
  loading:boolean;
  numberOfPlans:number;
  dialogRef: MatDialogRef<any, any>;


  constructor(public appService:AppService,private datepipe:DatePipe,private router:Router,private licenseServ:LicenseService,private dialogService:DialogService,private activeRoute: ActivatedRoute,private productService:ProductService, private loginService:LoginService, private userProfileService:UserProfileService) {
    let productName;
    this.activeRoute.paramMap.subscribe((params: ParamMap) => {
        productName = params.get('name');
    });
    this.productService.getProduct(productName).subscribe(
      (prd:any) => {this.product = prd; 
        let s= Object.keys(this.product.plansPrices); 
        this.numberOfPlans=s.length; 
        if(this.loginService.isLogged){
        this.getLicensesOfProductAndUser(); }},
      error => console.log(error)
    );
    this.successfulMessage=false;
    this.loading=false;
   }

   ngOnInit(): void {
    this.loadStripe();
    this.user=this.loginService.getUserLogged();

  } 
//METHODS TO SUBSCRIBE TO A PRODUCT
  subscribeToProduct(type:string,money:string){
    if(this.user==null){
     alert("You have to be logged first! If you don't have an account, you can register too");
  
    }else{
      this.dialogService.openConfirmDialog("You are going to subscribe to " + this.product.name + " with a " + type + " subscription. You will be charged now " + money + "€ to your default card.",true)
      .afterClosed().subscribe(
        res=>{
          if(res[0]){
            this.loading=true;
            this.userProfileService.addSubscriptionToProduct(this.product,type, this.user.name, res[1]).subscribe(
                (u:any)=> {this.successfulMessage=true;this.loading=false;this.serial=u.serial},
                error=> {this.treatmentBuyError(error);this.loading=false;},
            )
          }
        }
      )
    }

  }


  treatmentBuyError(error:any){
      if(error.status === 428){
          alert("You have to attach a payment source before buy a license");
      }else if (error.status === 409){
          alert("You already have a subscription of this type to this product!")
      }else{
        alert("There has been an error with the subscription and it couldn't be successful.")
      }
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

  pay(amount) {  
    if(this.user==null){
       alert("You have to be logged first! If you don't have an account, you can register too");
    }else{
      var handler = (<any>window).StripeCheckout.configure({
        key: this.appService.publicApiKey,
        currency:'eur',
        email:this.user.name +'@email.com',
        token: (token: any) =>{
          if(token!=null){
            this.confirm(token.id);
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


  confirm(token:string){
      this.userProfileService.buyProduct(token,this.product,this.user.name).subscribe(
        (t:any) => {this.successfulMessage=true; this.serial=t.serial;},
        error => {alert("The purchase has not been posible");}
      )


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


  formatDates(date:Date){
    return this.datepipe.transform(date, 'yyyy/MM/dd hh:MM'); 
  }


}
 