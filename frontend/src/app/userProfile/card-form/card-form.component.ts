import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { UserProfileService } from '../userProfile.service';
import { AppService } from '../../app.service';
import { Router } from '@angular/router';
import { UserProfileComponent } from '../userProfile.component';
import { UsedCardService } from '../../usedCard/usedCard.service';
import { LoginService } from '../../login/login.service';


declare var Stripe: any;

@Component({
  selector: 'app-card-form',
  templateUrl: './card-form.component.html',
  styleUrls: ['./card-form.component.css']
})
export class CardFormComponent implements OnInit {
  successfulAdd:boolean;
  loading:boolean;

  @Input()
  calledByTrial:boolean;

  @Input()
  productName:string;

  @Output() valid: EventEmitter<boolean> = new EventEmitter();
  @Output() token: EventEmitter<string> = new EventEmitter();
  @Output() cardParams: EventEmitter<any[] > = new EventEmitter();


  constructor(private loginServ:LoginService,private usedCardServ:UsedCardService,private router:Router,private appService:AppService,private userProfileServ:UserProfileService, private userProfileCom:UserProfileComponent) { }

  ngOnInit() {
    // Your Stripe public key
    this.appService.getPublicStripeKey().subscribe(
      k => this.setup(),
      error=>console.log(error)
    )    
  }

  setup(){
    const stripe = Stripe(this.appService.publicApiKey);

    // Create `card` element that will watch for updates
    // and display error messages
    const elements = stripe.elements();
    const card = elements.create('card');
    card.mount('#card-element');
    card.addEventListener('change', event => {
      const displayError = document.getElementById('card-errors');
      if (event.error) {
        displayError.textContent = event.error.message;
      } else {
        displayError.textContent = '';
      }
    });

    
    const paymentForm = document.getElementById('payment-form');
    paymentForm.addEventListener('submit', event => {
      this.loading=true;
      event.preventDefault();
      stripe.createToken(card).then(result => {
        if (result.error) {
          console.log('Error creating payment method.');
          const errorElement = document.getElementById('card-errors');
          errorElement.textContent = result.error.message;
          this.loading=false;
        } else {
          // At this point, you should send the token ID
          // to your server so it can attach
          // the payment source to a customer
          if(this.calledByTrial){
            this.usedCardServ.checkCard(result.token.card.last4, result.token.card.exp_month, result.token.card.exp_year,this.productName).subscribe(
              resp => {
                if(resp){
                  alert("This card has already been used to get a Free Trial Subscription for this product");
                  this.loading=false;
                  this.valid.emit(false);
                }else{
                  let params = [result.token.id,result.token.card.last4, result.token.card.exp_month, result.token.card.exp_year];
                  this.cardParams.emit(params);
                  this.valid.emit(true);
                  this.token.emit(result.token.id);
                  this.loading=false;
                }
              },
              error=>{ console.log(error); this.loading=false; this.valid.emit(false)}
            )
          }else{
            this.userProfileServ.addCardStripeElements(this.loginServ.user.name,result.token.id).subscribe(
              t=> {this.successfulAdd=true;this.loading=false; this.userProfileCom.getCards()},
              error=> {console.log(error);this.successfulAdd=false; this.loading=false;}
            );
          }

        }
      });
    });
  }

}
