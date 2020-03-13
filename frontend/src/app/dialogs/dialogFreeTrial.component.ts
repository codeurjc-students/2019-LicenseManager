import { Component, Inject } from "@angular/core";
import { ConfirmationDialogComponent } from "./confirmation-dialog/confirmation-dialog.component";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";

@Component({
    selector: 'app-dialogFreeTrial',
    templateUrl: './dialogFreeTrial.component.html',
    styleUrls: ['./dialogFreeTrial.component.css']

})


export class DialogFreeTrial {

    valid:boolean;
    token:string;
    params:any[];
    constructor(@Inject(MAT_DIALOG_DATA) public data, public dialogRef: MatDialogRef<DialogFreeTrial>){ 
        this.valid=false;
    }

    yes(){
        this.dialogRef.close(this.params);
    }

    closeDialog() {
        this.dialogRef.close(null);
    }

    setValid(valid:boolean){
        this.valid= valid;
    }

    setToken(token:string){
        this.token=token;
    }

    setParams(params:any[]){
        this.params=params;
    }
  
}