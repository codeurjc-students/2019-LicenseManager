import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { License } from './license.model';
import { Product } from '../product/product.model';

const BASE_URL = 'http://localhost:8080/api/';


@Injectable()
export class LicenseService {

    
    constructor(private http: Http) { }

    getLicensesOfUser(userName:string){
        let url = BASE_URL + "licenses/" + userName;
        return this.http.get(url)
			.map(response => response.json())
			.catch(error => this.handleError(error));
    }

    deleteLicense(serial:string,productName:string){
      let url = BASE_URL + "licenses/" + serial+"/"+productName;
      return this.http.delete(url)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }

    addLicense(license:License){
      let url = BASE_URL + "licenses/";
      return this.http.post(url,license)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }

    canceltAtEndLicense(serial:string,productName:string){
      let url = BASE_URL + "licenses/cancelAtEnd/"+serial+"/"+ productName;
      return this.http.put(url,null)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }



    updateLicense(license:License){
      let url = BASE_URL + "licenses/update/";
      return this.http.put(url,license)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }


    getOneLicense(serial:string,productName:string){
      let url = BASE_URL + "licenses/" + serial+"/"+productName;
      return this.http.get(url)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }

    getLicensesOfUserAndProduct(userName:string,productName:string){
      let url = BASE_URL + "licenses/user/"+userName+"/product/"+productName;
      return this.http.get(url)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }


    private handleError(error: any) {
        console.error(error);
      return Observable.throw("Server error (" + error.status + "): " + error.text())
  }
}