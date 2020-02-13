import { Component, OnInit, NgZone } from '@angular/core';

@Component({
  selector: 'app-custom-form-component',
  templateUrl: './custom-form-component.component.html',
  styleUrls: ['./custom-form-component.component.css']
})
export class CustomFormComponentComponent {

  cardNumber: string;
  expiryMonth: string;
  expiryYear: string;
  cvc: string;

  message: string;

  token:string;

  constructor(private _zone: NgZone) {}

  getToken() {
    this.message = 'Loading...';

    (<any>window).Stripe.card.createToken({
      number: this.cardNumber,
      exp_month: this.expiryMonth,
      exp_year: this.expiryYear,
      cvc: this.cvc
    }, (status: number, response: any) => {

      // Wrapping inside the Angular zone
      this._zone.run(() => {
        if (status === 200) {
          this.token = `${response.card.id}`;
          console.log(this.token);
        } else {
          this.message = response.error.message;
        }
      });
    });

  }



}
