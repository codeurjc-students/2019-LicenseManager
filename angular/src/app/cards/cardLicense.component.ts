import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { License } from '../licenses/license.model';
import { Product } from '../product/product.model';
import { ProductComponent } from '../product/product.component';
import { ProductService } from '../product/product.service';
import { LoginService } from '../login/login.service';
import { MatDialog, MatDialogRef } from '@angular/material';
import { LicenseService } from '../licenses/license.service';
import { TdChipsBase } from '@covalent/core';
import { DialogDeleteComponent } from '../dialogs/dialogDelete.component';
import { DialogEditComponent } from '../dialogs/dialogEdit.component';

@Component({
    selector: 'app-card-license',
    templateUrl: './cardLicense.component.html',
    styleUrls: ['./cardLicense.component.css'],

  })
  
  export class CardLicenseComponent{

    product?:Product;
    @ViewChild('addLicenseDialog') addLicenseDialog: TemplateRef<any>;

    dialogRef: MatDialogRef<any, any>; 
    typeAlertNull:boolean;
    statusAlertNull:boolean;


    constructor(public productService:ProductService,public licenseService:LicenseService, public loginService: LoginService,public dialog: MatDialog){
        this.typeAlertNull=false;
        this.statusAlertNull=false;
    }
    
    getLicenses(name:string){
    this.productService.getLicensesByProduct(name).subscribe(
        lics => {this.product.licenses=lics.content;},
        error => console.log(error)
    ); 

    }

    openAddDialog(){
        this.dialogRef = this.dialog.open(this.addLicenseDialog, {
          width: '50%',
          height: '40%',
      });
    }

    openDeleteDialog(s:string,product:string){
        this.dialogRef = this.dialog.open(DialogDeleteComponent, {
            data:{
                serial:s,
                productName:product
            },
        });
        this.dialogRef.afterClosed().subscribe(
            lics => {this.getLicenses(product)},
            error => console.log(error)
        );
    }


    openEditDialog(license:License){
        this.dialogRef = this.dialog.open(DialogEditComponent, {
            data:{
                license:license,
            },
        });
        this.dialogRef.afterClosed().subscribe(
            lics => {this.getLicenses(this.product.name)},
            error => console.log(error)
        );
    }


  /*
    addLicense(s:string,active:boolean,type:string){
        console.log(s,active,type);
        let lic :License = {serial: s,active:active,type:type, owner:null,product:this.product,};
        this.productService.addLicenseToProduct(lic).subscribe(
            lic=>{this.dialogRef.close();this.getLicenses(this.product.name)},
            error=> console.log(error)
        );
    }

    deleteLicense(serial:string,productName:string){
        this.licenseService.deleteLicense(serial,productName).subscribe(
            lic=>{this.getLicenses(this.product.name)},
            error=> console.log(error) 
        );
    }*/

    addLicense(s:string,active:boolean,type:string){
        if(active==null){
            this.statusAlertNull=true;
        }
        if(type==null){
            this.typeAlertNull=true;
        }
        if(type!=null && active !=null){
            console.log(s,active,type);
            let lic :License = {serial: s,active:active,type:type, owner:null,product:this.product,};
            this.licenseService.addLicense(lic).subscribe(
                lic=>{this.dialogRef.close();this.getLicenses(this.product.name)},
                error=> console.log(error)
            );
            this.statusAlertNull=false;
            this.typeAlertNull=false;
        }

    }

 

    changeStatus(serial:string){
        this.licenseService.changeStatus(serial,this.product.name).subscribe(
            ans=>{this.getLicenses(this.product.name)},
            error=>console.log(error),
        );
    }

    getOneLicense(serial:string, productName:string):License{
        let license:License;
        this.licenseService.getOneLicense(serial,productName).subscribe(
            lic=>{license=lic},
            error=> console.log(error)
        );
        return license;
    }
    
  }


