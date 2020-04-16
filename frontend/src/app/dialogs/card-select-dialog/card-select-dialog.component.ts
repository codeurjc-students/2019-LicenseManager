import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserProfileService } from '../../userProfile/userProfile.service';

@Component({
    selector: 'app-dialog-card-select',
    templateUrl: './card-select-dialog.component.html',
    styleUrls: ['./card-select-dialog.component.css']

})
export class CardSelectDialog {

    paymentMethods:any[];
    pmSelected:any;
    loading:boolean = false;
    defaultPM:string;
    res:any[]=[];
    checkboxStatus:boolean=false;


    constructor(@Inject(MAT_DIALOG_DATA) public data,public dialogRef: MatDialogRef<CardSelectDialog>, private userProfileServ:UserProfileService){ 
        this.loading=true;
        this.userProfileServ.getUserCardsStripe(data.user).subscribe(
            (cards:any) => {this.paymentMethods=cards;this.loading=false; this.userProfileServ.getDefaultCard(data.user).subscribe(
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
}