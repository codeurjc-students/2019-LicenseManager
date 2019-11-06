import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { License } from '../licenses/license.model';
import { ProductService } from './product.service';
import { ActivatedRoute, Router, ParamMap } from '@angular/router';
import { LoginService } from '../login/login.service';
import { Product } from './product.model';
import { MatDialog, MatDialogRef } from '@angular/material';
import { CardLicenseComponent } from '../cards/cardLicense.component';

@Component({
    selector: 'app-product',
    templateUrl: './product.component.html',
    styleUrls: ['./product.component.css'],
  })
  export class ProductComponent implements OnInit{
    @ViewChild('cardLicense') cardLicense: CardLicenseComponent;
    product:Product;
    serial?:string="";
    active?:boolean;
    type?:string;

    constructor(private router: Router, private activeRoute: ActivatedRoute,private productService:ProductService,  public loginService: LoginService){    }

    ngOnInit(): void {

      this.activeRoute.paramMap.subscribe((params: ParamMap) => {
        let name = this.activeRoute.snapshot.params.name;
        this.getProduct(name);
      });


    }

    getProduct(name:string){
      this.productService.getProduct(name).subscribe(
        prod => {this.product = prod;this.cardLicense.product=prod; this.cardLicense.getLicenses(this.product.name)},
        error => console.log(error)
      )
    }



    configCardLicense(cardLicense:CardLicenseComponent){
      cardLicense.product=this.product;
    }

    deleteLicense(){}

    startLicense(){}

    pauseLicense(){}





  }