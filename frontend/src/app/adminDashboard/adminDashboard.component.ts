import { Component, ViewChild, TemplateRef } from "@angular/core";
import { Product } from "../product/product.model";
import { ProductService } from "../product/product.service";
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef, MatSnackBar, MatSnackBarConfig } from '@angular/material';
import { DialogAddProductComponent } from "../dialogs/dialogAddProduct.component";
import { DialogService } from '../dialogs/dialog.service';

@Component({
    selector: 'app-adminDashboard',
    templateUrl: './adminDashboard.component.html',
    styleUrls: ['./adminDashboard.component.css']
  })
  export class AdminDashboardComponent {
    @ViewChild('addProductDialog',{static:false}) searchDialog: TemplateRef<any>;
    dialogRef: MatDialogRef<any, any>;
    products:Product[];

    pageActual:number = 1;
    numberOfElements = 5;

    conf:MatSnackBarConfig= new MatSnackBarConfig();

    constructor(private snackbar:MatSnackBar,private dialogService:DialogService,public dialog: MatDialog,private productService:ProductService,private router: Router){
        this.getProducts();
        this.conf.duration=3000;
    }

    getProducts(){
        this.productService.getProducts().subscribe(
            (resp:any)  => {this.products = resp.content},
            error => console.log(error)
        );
    }

    openAddProductDialog(){
        this.dialogRef = this.dialog.open(DialogAddProductComponent, {
            data:{
                type:"add",
            },
        });
        this.dialogRef.afterClosed().subscribe(
            (p:any) => {if(p){this.getProducts();this.snackbar.open("Product added","X",this.conf)}}

        );
    }

    openEditProductDialog(prod:Product){
        this.dialogRef = this.dialog.open(DialogAddProductComponent, {
            data:{
                type:"edit",
                product:prod,
            },
        });
        this.dialogRef.afterClosed().subscribe(
            p =>{ this.getProducts(); if(p){this.snackbar.open("Product updated","X",this.conf)}}

        );
    }

    deleteProduct(prod:Product){
        this.dialogService.openConfirmDialog("This action will set the product "+prod.name+" to inactive, so new customers won't be able to purchase or subscribe to it and it won't appear on the catalog, but customers that already have it will still be able to use it. Are you sure you want to do this?",false,true)
        .afterClosed().subscribe(
            res=> {
                if (res[0]){
                    this.productService.deleteProduct(prod).subscribe(
                    s=> {this.getProducts();this.snackbar.open("Product deleted","X",this.conf)},
                    error => console.log(error),)
                }
            }
            
        )

        
    }

  }