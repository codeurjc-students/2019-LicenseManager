import { Component, Inject } from '@angular/core';
import { User } from '../login/login.service';
import { Product } from '../product/product.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { ProductService } from '../product/product.service';

@Component({
    selector: 'app-dialogSearch',
    templateUrl: './dialogSearch.component.html',
    })
    export class DialogSearchComponent  {
        user:User;
        listProducts:Product[];
        productSelected:Product;



    constructor( @Inject(MAT_DIALOG_DATA) public data:any, private dialogRef:MatDialogRef<DialogSearchComponent>, public productService:ProductService){
        this.user=data.user;
        this.productService.getProducts().subscribe(
            products => {this.listProducts=products.content},
            error=> console.log(error)
        )

    }








    close(){
        this.dialogRef.close();
    }

    }