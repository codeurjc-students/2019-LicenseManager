import { Component, OnInit } from '@angular/core';
import { User, LoginService } from '../login/login.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { UserProfileService } from './userProfile.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { StripeCard } from '../stripe/stripeCard.model';

@Component({
    selector: 'app-userProfile',
    templateUrl: './userProfile.component.html',
    styleUrls: ['./userProfile.component.css']

  })
  export class UserProfileComponent implements OnInit{
    user:User;
    stripeCards:StripeCard[];

    owner = new FormControl('',Validators.required);
    number = new FormControl('',[Validators.required, Validators.minLength(16), Validators.maxLength(16), Validators.pattern('[0-9]*')]);
    cvv = new FormControl('',[Validators.required,Validators.minLength(3), Validators.maxLength(3),Validators.pattern('[0-9]*')]);
    exp_month = new FormControl('',Validators.required);
    exp_year = new FormControl('',Validators.required);

    
    constructor( private loginService:LoginService, private userProfileServ:UserProfileService){
      this.getCards();
    }


    ngOnInit(): void {
      this.user=this.loginService.user;
    }

    addCard(){
      console.log(this.cvv);
      
      this.userProfileServ.addCard(this.owner.value,this.cvv.value,this.number.value,this.exp_month.value,this.exp_year.value).subscribe(
        (u) => {
            this.updateCards();
            
        },
        (error) => console.log(error),
    );    
    }

    updateCards(){
      this.userProfileServ.updateCards().subscribe(
         u =>   this.getCards(),  
        (error) => console.log(error),
      );
    }

    getCards(){
      this.userProfileServ.getCards().subscribe(
        cards => this.stripeCards = cards.content,
      )
    }

    try(){
      console.log(this.stripeCards);
    }


    
  }