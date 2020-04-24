import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserProfileService } from '../../userProfile/userProfile.service';

@Component({
    selector: 'app-dialog-card-subscription-select',
    templateUrl: './card-subscription-select-dialog.component.html',
    styleUrls: ['./card-subscription-select-dialog.component.css']

})
export class CardSubscriptionSelectDialog {

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


    constructor(@Inject(MAT_DIALOG_DATA) public data,public dialogRef: MatDialogRef<CardSubscriptionSelectDialog>, private userProfileServ:UserProfileService){ 
        this.loading=true;
        this.user=data.user;
        this.subsId=data.subsId;
        this.getCards(this.user, this.subsId);

    }

    getCards(user:any, subsId:string){
        this.userProfileServ.getUserCardsStripe(user).subscribe(
            (cards:any) => {this.paymentMethods=cards;this.loading=false; this.userProfileServ.getPaymentMethodOfSubs(subsId,user).subscribe(
                (def:any) => {this.defaultPM=def.response; this.pmSelected=this.defaultPM} 
            )},
            error=> {console.log(error);this.loading=false}
        )
    }

    yes(){
        this.res[0]=true;
        this.res[1]=this.pmSelected;
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
        this.getCards(this.user,this.subsId);
    }
}