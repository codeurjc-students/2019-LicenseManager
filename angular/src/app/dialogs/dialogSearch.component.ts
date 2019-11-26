import { Component, Inject } from '@angular/core';
import { User } from '../login/login.service';
import { Product } from '../product/product.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { ProductService } from '../product/product.service';
import { UserProfileService } from '../userProfile/userProfile.service';

@Component({
    selector: 'app-dialogSearch',
    templateUrl: './dialogSearch.component.html',
    })
    export class DialogSearchComponent  {
        user:User;
        listProducts:Product[];
        productSelected:Product;
        types:string[];
        typeSubSelected:string;
        loading:boolean;

    constructor( @Inject(MAT_DIALOG_DATA) public data:any, private dialogRef:MatDialogRef<DialogSearchComponent>, public productService:ProductService, public userService:UserProfileService){
        this.user=data.user;
        this.loading=false;
        this.productService.getProducts().subscribe(
            products => {this.listProducts=products.content},
            error=> console.log(error)
        )

    }

    buyProduct(){
        this.loading=true;
        this.userService.addSubscriptionToProduct(this.productSelected,this.typeSubSelected).subscribe(
            u=> {this.dialogRef.close();this.loading=false},
            error=> {this.dialogRef.close();this.treatmentBuyError(error);},
        )
    }





    treatmentBuyError(error:any){
        if(error.status === 428){
            console.log("dentro error");
            alert("You have to attach a payment source before buy a license");
        }
    }
    close(){
        this.dialogRef.close();
    }

    }