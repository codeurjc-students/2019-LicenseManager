import { Component, ViewChild, TemplateRef } from "@angular/core";
import { Product } from "../product/product.model";
import { ProductService } from "../product/product.service";
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef } from "@angular/material";
import { DialogAddProductComponent } from "../dialogs/dialogAddProduct.component";

@Component({
    selector: 'app-adminDashboard',
    templateUrl: './adminDashboard.component.html',
  })
  export class AdminDashboardComponent {
    @ViewChild('addProductDialog',{static:false}) searchDialog: TemplateRef<any>;
    dialogRef: MatDialogRef<any, any>;
    products:Product[];

    constructor(public dialog: MatDialog,productService:ProductService,private router: Router){
        productService.getProducts().subscribe(
            resp => this.products = resp.content,
            error => console.log(error)
        );
    }

    openAddProductDialog(){
        this.dialogRef = this.dialog.open(DialogAddProductComponent, {
            data:{
                
            },
        });
        this.dialogRef.afterClosed().subscribe(

        );
    }

  }