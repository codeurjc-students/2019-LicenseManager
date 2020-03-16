import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { License } from '../licenses/license.model';
import { Product } from '../product/product.model';
import { ProductComponent } from '../product/product.component';
import { ProductService } from '../product/product.service';
import { LoginService } from '../login/login.service';
import { MatDialog, MatDialogRef } from '@angular/material';
import { LicenseService } from '../licenses/license.service';
import { TdChipsBase } from '@covalent/core';

@Component({
    selector: 'app-card-license',
    templateUrl: './cardLicense.component.html',
    styleUrls: ['./cardLicense.component.css'],

  })
  
  export class CardLicenseComponent{

    product?:Product;
    @ViewChild('addLicenseDialog',{static:false}) addLicenseDialog: TemplateRef<any>;

    dialogRef: MatDialogRef<any, any>; 
    typeAlertNull:boolean;
    statusAlertNull:boolean;

    pageActual:number = 1;
    numberOfElements = 5;
    constructor(public productService:ProductService,public licenseService:LicenseService, public loginService: LoginService,public dialog: MatDialog){
        this.typeAlertNull=false;
        this.statusAlertNull=false;
    }
    
    getLicenses(name:string){
    this.productService.getLicensesByProduct(name).subscribe(
        (lics:any) => {this.product.licenses=lics.content;},
        error => console.log(error)
    ); 

    }

    openAddDialog(){
        this.dialogRef = this.dialog.open(this.addLicenseDialog, {
          width: '50%',
          height: '40%',
      });
    }



 

    canceltAtEndLicense(serial:string){
        this.licenseService.canceltAtEndLicense(serial,this.product.name).subscribe(
            ans=>{this.getLicenses(this.product.name)},
            error=>console.log(error),
        );
    }

    getOneLicense(serial:string, productName:string):License{
        let license:License;
        this.licenseService.getOneLicense(serial,productName).subscribe(
            (lic:any)=>{license=lic},
            error=> console.log(error)
        );
        return license;
    }
    
  }


