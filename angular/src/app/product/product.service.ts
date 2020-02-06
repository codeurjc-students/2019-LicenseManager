import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Rx';
import { License } from '../licenses/license.model';
import { Product } from './product.model';

const BASE_URL = 'http://localhost:8080/api/';

@Injectable()
export class ProductService {

    constructor(private http: Http) { }
    
    getLicensesByProduct(productName:string){
        let url = BASE_URL + "licenses/product/" + productName;
        return this.http.get(url)
			.map(response => response.json())
			.catch(error => this.handleError(error));

    }

    getProduct(productName:string){
      let url = BASE_URL + "product/" + productName;
        return this.http.get(url)
			.map(response => response.json())
			.catch(error => this.handleError(error));
    }

    postProduct(product:Product){
      let url = BASE_URL + "product/";
      return this.http.post(url,product)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }


    addLicenseToProduct(license:License){
      let url = BASE_URL + "product/" + license.product.name;
      return this.http.put(url,license)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }

    removeLicenseOfProduct(serial:string,productName:string){
      let url = BASE_URL + "product/" + productName+"/license/"+serial;
      return this.http.put(url,productName,serial)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }

    getProducts(){
      let url = BASE_URL + "product/all";
      return this.http.get(url)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }

    getProductSearch(search:string){
      let url = BASE_URL + "product/all/?search=" + search;
      return this.http.get(url)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }





    private handleError(error: any) {
		  console.error(error);
		  return Observable.throw("Server error (" + error.status + "): " + error.text())
	  }
}