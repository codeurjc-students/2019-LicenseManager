import { Component, OnInit } from '@angular/core';
import { Product } from '../../product/product.model';
import { ProductService } from '../../product/product.service';
import { ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-catalog-product',
  templateUrl: './catalog-product.component.html',
  styleUrls: ['./catalog-product.component.css']
})
export class CatalogProductComponent implements OnInit {

  product?:Product;

  constructor(private activeRoute: ActivatedRoute,private productService:ProductService) {
    let productName;
    this.activeRoute.paramMap.subscribe((params: ParamMap) => {
        productName = params.get('name');
    });
    this.productService.getProduct(productName).subscribe(
      prd => {this.product = prd;},
      error => console.log(error)
    );
   }

  ngOnInit() {
    
  }

  buy(type:string){
    
  }

}
 