import { Component, OnInit, Input, OnChanges, SimpleChanges, Renderer } from '@angular/core';
import { Product } from '../../product/product.model';
import { ProductService } from '../../product/product.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { LoginService, User } from '../../login/login.service';
import { UserProfileService } from '../../userProfile/userProfile.service';
import { DialogService } from 'src/app/dialogs/dialog.service';


@Component({
  selector: 'app-catalog-product',
  templateUrl: './catalog-product.component.html',
  styleUrls: ['./catalog-product.component.css']
})
export class CatalogProductComponent implements OnInit {



  product?:Product;
  user:User;
  errorAdded:boolean;
  card:boolean;
  successfulMessage:boolean;
  serial:string;
  tk:string;
  loading:boolean;
  numberOfPlans:number;

  constructor(private dialogService:DialogService,private activeRoute: ActivatedRoute,private productService:ProductService, private loginService:LoginService, private userProfileService:UserProfileService) {
    let productName;
    this.activeRoute.paramMap.subscribe((params: ParamMap) => {
        productName = params.get('name');
    });
    this.productService.getProduct(productName).subscribe(
      prd => {this.product = prd; let s= Object.keys(this.product.plansPrices); this.numberOfPlans=s.length},
      error => console.log(error)
    );
    this.errorAdded=false;
    this.card=false;
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
      this.dialogService.openConfirmDialog("You are going to subscribe to " + this.product.name + " with a " + type + " subscription. You will be charged now " + money + "€ to your default card")
      .afterClosed().subscribe(
        res=>{
          if(res){
            this.loading=true;
            this.userProfileService.addSubscriptionToProduct(this.product,type, this.user.name).subscribe(
                u=> {;this.loading=false},
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
    localStorage.removeItem("token");
    localStorage.removeItem("added");

    if(this.user==null){
      alert("You have to be logged first! If you don't have an account, you can register too");
    }else{
      var handler = (<any>window).StripeCheckout.configure({
        key: 'pk_test_mAWlLKZrpvwEye9QZzRZzsSG00M6tsJ3QS',
        currency:'eur',
        token: function (token: any) {
          if(token!=null){
            localStorage.setItem("token",token.id);
            localStorage.setItem("added","true");
          }
        }

      });
  
      handler.open({
        name: "Set your payment card info", 
        description: 'Click "Pay" and "Confirm Order" later',
        amount: amount * 100
      });
      this.errorAdded=false;
      this.card=true;

    }
  
  }


  confirm(){
    let added = localStorage.getItem("added");
    if(added=="true"){
      let token = localStorage.getItem("token");
      this.userProfileService.buyProduct(token,this.product,this.user.name).subscribe(
        t => {this.successfulMessage=true; this.serial=t.serial},
        error => console.log(error)
      )
    }else{
      this.errorAdded=true;
      this.card=false;
    }

  }




}
 