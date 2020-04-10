import { Injectable } from "@angular/core";
import { MatDialog } from '@angular/material';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
import { LoginComponent } from '../login/login.component';
import { DialogFreeTrial } from './dialogFreeTrial.component';
import { AddCardDialogComponent } from './add-card-dialog/add-card-dialog.component';
import { CardSelectDialog } from './card-select-dialog/card-select-dialog.component';

@Injectable({
    providedIn:'root'
})

export class DialogService {
    
    constructor(private dialog:MatDialog){}


    openConfirmDialog(msg,cb:boolean, noButton:boolean,width:string="390px"){
        return this.dialog.open(ConfirmationDialogComponent,{
            panelClass: 'confirm-dialog-container',
            width: width,
            disableClose: true,
            data :{
              message : msg,
              checkbox:cb,
              noButton: noButton,
              checkboxStatus:null,
            }
          });
    }

    openLoginDialog(){
      this.dialog.open(LoginComponent);
    }

    openAddCardDialog(){
      return this.dialog.open(AddCardDialogComponent);
    }

    openCardSelectDialog(userName:string, msg:string, cb:boolean){
      return this.dialog.open(CardSelectDialog,{
        width: '60vw',
        data:{
          user: userName,
          msg: msg,
          checkbox:cb
        }
      });
    }

    openFreeTrialDialog(productName:string){
      return this.dialog.open(DialogFreeTrial,{
        width: '390px',
            panelClass: 'confirm-dialog-container',
            disableClose: true,
            data :{
              productName : productName,
            }
      })
    }
}