import { Component, OnInit } from '@angular/core';
import { Product } from '../product/product.model';
import { ProductService } from '../product/product.service';
import { Router } from '@angular/router';

const BASE_URL_PRODUCT = "/api/products/"

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit{
  

  products:Product[];
  searchInputTerm:string;
  pageActual:number = 1;
  numberOfElements = 5;

  constructor(private productService:ProductService, private router:Router){}

  ngOnInit(): void {
    this.productService.getProducts().subscribe( 
      (resp:any) =>{this.products = resp},
      error => console.log(error)
    );
  }

  goToProduct(name:string){
    this.router.navigate(["/products/",name]);
  }

  searchProduct(){
    this.productService.getProductSearch(this.searchInputTerm).subscribe(
      (ps:any) => this.products = ps,
      error => console.log(error)
    )
  }

  pathPhotos(productName:string){
    return BASE_URL_PRODUCT + productName +"/image";
  }

}
