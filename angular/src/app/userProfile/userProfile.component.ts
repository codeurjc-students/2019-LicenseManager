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

    
    constructor( private loginService:LoginService){ }


    ngOnInit(): void {
      this.user=this.loginService.user;
    }
  }