import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserProfileService } from '../../userProfile/userProfile.service';

@Component({
    selector: 'app-dialog-card-select',
    templateUrl: './card-select-dialog.component.html',
    styleUrls: ['./card-select-dialog.component.css']

})
export class CardSelectDialog implements OnInit{

    paymentMethods:any[];
    pmSelected:any;
    loading:boolean = false;
    defaultPM:string;
    res:any[]=[];
    checkboxStatus:boolean=false;
    inserted:boolean=false;
    cardAdded:boolean = false;
    user:string;
    subsId:string;


    constructor(@Inject(MAT_DIALOG_DATA) public data,public dialogRef: MatDialogRef<CardSelectDialog>, private userProfileServ:UserProfileService){ 
        this.user=data.user;
        if(data.subsId!=null){
            this.subsId = data.subsId;
        }
    }
    ngOnInit(): void {
        this.loading=true;
        if(this.subsId!=null){
            this.getCards2(this.user, this.subsId);
        }else{
            this.getCards(this.user);
        }
    }

    
    getCards2(user:any, subsId:string){
        this.userProfileServ.getUserCardsStripe(user).subscribe(
            (cards:any) => {this.paymentMethods=cards;this.loading=false; this.userProfileServ.getPaymentMethodOfSubs(subsId,user).subscribe(
                (def:any) => {this.defaultPM=def.response; this.pmSelected=this.defaultPM} 
            )},
            error=> {console.log(error);this.loading=false}
        )
    }

    getCards(user:any){
        this.userProfileServ.getUserCardsStripe(user).subscribe(
            (cards:any) => {this.paymentMethods=cards;this.loading=false; this.userProfileServ.getDefaultCard(user).subscribe(
                (def:any) => {this.defaultPM=def.response; this.pmSelected=this.defaultPM} 
            )},
            error=> {console.log(error);this.loading=false}
        )
    }

    yes(){
        this.res[0]=true;
        this.res[1]=this.checkboxStatus;
        this.res[2]=this.pmSelected;
        this.dialogRef.close(this.res);
    }

    closeDialog() {
        this.res[0]=false;
        this.dialogRef.close(this.res);
    }

    insertCard(){
        this.inserted=true;
    }

    cardAddedHandler($event:any){
        this.cardAdded=$event;
        this.getCards(this.user);
    }
}