import { Component, OnInit } from '@angular/core';
import { User, LoginService } from '../login/login.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { UserProfileService } from './userProfile.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { StripeCard } from '../stripe/stripeCard.model';
import { DialogService } from '../dialogs/dialog.service';

@Component({
    selector: 'app-userProfile',
    templateUrl: './userProfile.component.html',
    styleUrls: ['./userProfile.component.css']

  })


  export class UserProfileComponent{
    user:User;
    stripeCards:any[];
    loading:boolean;

    owner = new FormControl('',Validators.required);
    number = new FormControl('',[Validators.required, Validators.minLength(16), Validators.maxLength(16), Validators.pattern('[0-9]*')]);
    cvv = new FormControl('',[Validators.required,Validators.minLength(3), Validators.maxLength(3),Validators.pattern('[0-9]*')]);
    exp_month = new FormControl('',Validators.required);
    exp_year = new FormControl('',Validators.required);

    
    constructor(private dialogService:DialogService,private userProfileService:UserProfileService,private activeRoute:ActivatedRoute,private loginService:LoginService){
      this.user=this.loginService.user;
      this.getCards();
     }
 
     getCards(){
      this.loading=true;
      this.userProfileService.getUserCardsStripe(this.user.name).subscribe(
        (cards:any)=> {this.stripeCards=cards;this.loading=false;},
        error=> {console.log(error);this.loading=false;}
      )
     }

     firstStepDelete(cardId:string){
      this.dialogService.openConfirmDialog("Do you want to remove this card?",false)
      .afterClosed().subscribe(
        res=>{
          if(res[0]){
            this.deleteCard(cardId);
          }
        }
      )
     }


     deleteCard(cardId:string){
       this.loading=true;
        this.userProfileService.deleteStripeCard(this.user.name,cardId).subscribe(
          w => {this.getCards();},
          error => {console.log(error);this.loading=false;}
        )
     }


  }