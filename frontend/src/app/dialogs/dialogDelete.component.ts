import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { LicenseService } from '../licenses/license.service';
import { ProductService } from '../product/product.service';

@Component({
    selector: 'app-dialogDelete',
    templateUrl: './dialogDelete.component.html',
  })
  export class DialogDeleteComponent  {
      serial:string;
      productName:string;


    constructor( @Inject(MAT_DIALOG_DATA) public data:any, public  licenseService:LicenseService, private dialogRef:MatDialogRef<DialogDeleteComponent>){
        this.serial=data.serial;
        this.productName=data.productName;
    }

    deleteLicense(){
        this.licenseService.deleteLicense(this.serial,this.productName).subscribe(
            lic=>{this.dialogRef.close();},
            error=> console.log(error)
        );
    }

    close(){
        this.dialogRef.close();
    }

  }