import { Component, OnInit } from '@angular/core';
import { UserProfileService } from '../userProfile.service';
import { AppService } from '../../app.service';


declare var Stripe: any;

@Component({
  selector: 'app-card-form',
  templateUrl: './card-form.component.html',
  styleUrls: ['./card-form.component.css']
})
export class CardFormComponent implements OnInit {
  successfulAdd:boolean;
  loading:boolean;

  constructor(private appService:AppService,private userProfileServ:UserProfileService) { }

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

            this.userProfileServ.addCardStripeElements(result.token.id).subscribe(
              t=> {this.successfulAdd=true;this.loading=false;},
              error=> {this.successfulAdd=false; this.loading=false;}
            );
          

        }
      });
    });
  }

}
