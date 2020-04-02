import { Component } from "@angular/core";
import { MatDialogRef } from "@angular/material";

@Component({
    selector: 'add-card-dialog',
    templateUrl: './add-card-dialog.component.html',
    styleUrls: ['./add-card-dialog.component.css']
  })
  export class AddCardDialogComponent {
    cardAdded:boolean = false;

    constructor(public dialogRef: MatDialogRef<AddCardDialogComponent>){}

    closeDialog() {
        this.dialogRef.close(false);
    }

    yes(){
    this.dialogRef.close(true);
    }

    cardAddedHandler($event:any){
        console.log($event);
        this.cardAdded=$event;
    }
} 