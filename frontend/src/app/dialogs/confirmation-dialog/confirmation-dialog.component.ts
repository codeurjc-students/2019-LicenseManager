import { Component, OnInit, Input, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.css']
})
export class ConfirmationDialogComponent{
  exit:boolean[]=[];
  checkboxStatus:boolean;
  noButton:boolean;
  constructor(@Inject(MAT_DIALOG_DATA) public data, public dialogRef: MatDialogRef<ConfirmationDialogComponent>) { 
    if(data.checkbox){
      this.checkboxStatus=false;
    }
    if(data.noButton){
      this.noButton=true;
    }
  }

  ngOnInit() {
  }

  closeDialog() {
    this.dialogRef.close(false);
  }

  yes(){
    this.exit[0]=true;
    this.exit[1]=this.checkboxStatus;
    this.dialogRef.close(this.exit);
  }

  no(){
    this.exit[0] =false;
    this.dialogRef.close(this.exit);

  }


}
