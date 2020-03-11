import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Rx';
import { License } from '../licenses/license.model';
import { Product } from './product.model';
import { HttpClient } from '@angular/common/http';

const BASE_URL = 'https://localhost:8443/api/';

@Injectable()
export class ProductService {

    constructor(private http: HttpClient) { }
    
    getLicensesByProduct(productName:string){
        let url = BASE_URL + "licenses/products/" + productName;
        return this.http.get(url)
    }

    getProduct(productName:string){
      let url = BASE_URL + "products/" + productName;
        return this.http.get(url)
    }

    postProduct(product:Product){
      let url = BASE_URL + "products/";
      return this.http.post(url,product)
    }

    putProduct(product:Product){
      let url = BASE_URL + "products/";
      return this.http.put(url,product)
    }

    deleteProduct(product:Product){
      let url = BASE_URL + "products/"+product.name;
      return this.http.delete(url)
    }

    getImage(productName:string){
      let url = BASE_URL + "products/" + productName + "/image";
        return this.http.get(url)
    }

    addImage(file:File,productName: string) {
      let formdata: FormData = new FormData();
        formdata.append('file', file);
          return this.http.post(BASE_URL + "products/" + productName + "/image",formdata);
    }


    //Just the active ones
    getProducts(){
      let url = BASE_URL + "products";
      return this.http.get(url)
    }

    getProductSearch(search:string){
      let url = BASE_URL + "products?search=" + search;
      return this.http.get(url)
    }





    private handleError(error: any) {
		  console.error(error);
		  return Observable.throw("Server error (" + error.status + "): " + error.text())
	  }
}