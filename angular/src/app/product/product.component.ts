import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { License } from '../licenses/license.model';
import { ProductService } from './product.service';
import { ActivatedRoute, Router, ParamMap } from '@angular/router';
import { LoginService } from '../login/login.service';
import { Product } from './product.model';
import { MatDialog, MatDialogRef } from '@angular/material';

@Component({
    selector: 'app-product',
    templateUrl: './product.component.html',
    styleUrls: ['./product.component.css'],
  })
  export class ProductComponent implements OnInit{
    @ViewChild('addLicenseDialog') addLicenseDialog: TemplateRef<any>;
    dialogRef: MatDialogRef<any, any>; 
    product:Product;

    serial?:string="";
    active?:boolean;
    type?:string;

    constructor(private router: Router, private activeRoute: ActivatedRoute,private productService:ProductService,  public loginService: LoginService,public dialog: MatDialog){    }

    ngOnInit(): void {

      this.activeRoute.paramMap.subscribe((params: ParamMap) => {
        let name = this.activeRoute.snapshot.params.name;
        this.getProduct(name);
      });


    }

    getProduct(name:string){
      this.productService.getProduct(name).subscribe(
        prod => {this.product = prod; this.getLicenses(name)},
        error => console.log(error)
      )
    }

    getLicenses(name:string){
      this.productService.getLicensesByProduct(name).subscribe(
        lics => {this.product.licenses=lics.content;},
        error => console.log(error)
      ); 
 
    }

    deleteLicense(){}

    startLicense(){}

    pauseLicense(){}



    openAddDialog(){
      this.dialogRef = this.dialog.open(this.addLicenseDialog, {
        width: '50%',
        height: '40%',
    });
    }

    addLicense(s:string,active:boolean,type:string){
      console.log(s,active,type);
      let lic :License = {serial: s,active:active,type:type, owner:null,product:this.product,};
      this.productService.addLicenseToProduct(lic).subscribe(
        lic=>console.log(lic),
        error=> console.log(error)
      );
    }

  }