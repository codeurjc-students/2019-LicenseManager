import { Component } from "@angular/core";
import { Product } from '../product/product.model';
import { MatDialogRef } from '@angular/material';
import { License } from '../licenses/license.model';
import { ProductService } from '../product/product.service';

@Component({
    selector: 'app-dialogAddProduct',
    templateUrl: './dialogAddProduct.component.html',
    styleUrls: ['./dialogAddProduct.component.css']

  })
  export class DialogAddProductComponent {

    lifetime:boolean;
    annual:boolean;
    monthly:boolean;
    daily:boolean;
    type:string;
    name:string;
    description:string;
    webLink:string;
    priceDaily:number;
    priceMonthly:number;
    priceAnnual:number;
    price:number;

    constructor(private dialogRef:MatDialogRef<DialogAddProductComponent>, private productServ:ProductService){
      this.daily=false;
      this.annual=false;
      this.monthly=false;
    }
      
  close(){
      this.dialogRef.close();
  }

  save(){
    let licenses:License[];
    licenses=[];
    let typeSubs:string[];
    typeSubs = [];
    let plansPricesN:{ [name: string]: number }={};
    if (this.type==='subscription'){
      if (this.daily){
        typeSubs.push('D');
        plansPricesN['D']=this.priceDaily;
      }
      if (this.monthly){
        typeSubs.push('M');
        plansPricesN['M']=this.priceMonthly;
      }
      if (this.annual){
        typeSubs.push('A');
        plansPricesN['A']=this.priceAnnual;
      }
    }else if(this.type==='lifetime'){
      typeSubs.push('L');
      plansPricesN['L']=this.price;
    }
    let prod:Product = {name: this.name, licenses:licenses, typeSubs:typeSubs,photoAvailable:false,description: this.description,webLink:this.webLink,photoSrc:null,plansPrices:plansPricesN,sku:null};
    this.productServ.postProduct(prod).subscribe(
      g => {this.dialogRef.close()},
      error => console.log(error)
    )
  }

  }