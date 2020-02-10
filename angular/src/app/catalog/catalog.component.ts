import { Component, OnInit } from '@angular/core';
import { Product } from '../product/product.model';
import { ProductService } from '../product/product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent {

  products:Product[];
  searchInputTerm:string;

  constructor(private productService:ProductService, private router:Router){
      productService.getProducts().subscribe(
          resp => this.products = resp.content,
          error => console.log(error)
      );
  }

  goToProduct(name:string){
    this.router.navigate(["/catalog/product/",name]);
  }

  searchProduct(){
    this.productService.getProductSearch(this.searchInputTerm).subscribe(
      ps => this.products = ps.content,
      error => console.log(error)
    )
  }

}
