import { Component, ViewChild, TemplateRef } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material';
import { License } from './license.model';

@Component({
    selector: 'app-license',
    templateUrl: './license.component.html',
    styleUrls: ['./license.component.css'],
  })
  
  export class LicenseComponent{
  //  @ViewChild('addLicenseDialog') addLicenseDialog: TemplateRef<any>;
 //   dialogRef: MatDialogRef<any, any>; 
    

    constructor(public dialog: MatDialog){}
/*
    openAddDialog(){
      this.dialogRef = this.dialog.open(this.addLicenseDialog, {
        width: '50%',
        height: '40%',
    });
    }*/
  }