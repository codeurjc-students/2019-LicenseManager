import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Rx';

const BASE_URL = 'http://localhost:8080/api/';

@Injectable()
export class ProductService {

    constructor(private http: Http) { }
    
    getLicensesByProduct(productName:string){
        let url = BASE_URL + "license/product/" + productName;
        return this.http.get(url)
			.map(response => response.json())
			.catch(error => this.handleError(error));

    }

    private handleError(error: any) {
		console.error(error);
		return Observable.throw("Server error (" + error.status + "): " + error.text())
	}
}