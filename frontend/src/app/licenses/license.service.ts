import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { License } from './license.model';
import { HttpClient } from '@angular/common/http';

const BASE_URL = 'https://localhost:8443/api/licenses/';


@Injectable()
export class LicenseService {

    
    constructor(private http: HttpClient) { }

    getLicensesOfUser(userName:string){
        let url = BASE_URL +"users/"+ userName;
        return this.http.get(url)
    }
    
    canceltAtEndLicense(serial:string,productName:string){
      let url = BASE_URL +"cancelAtEnd/"+serial+"/products/"+ productName;
      return this.http.put(url,null)
    }



    getOneLicense(serial:string,productName:string){
      let url = BASE_URL  + serial+"/products/"+productName;
      return this.http.get(url)
    }

    getLicensesOfUserAndProduct(userName:string,productName:string){
      let url = BASE_URL + "users/"+userName+"/products/"+productName;
      return this.http.get(url)
    }

}