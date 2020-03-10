import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { License } from './license.model';
import { HttpClient } from '@angular/common/http';

const BASE_URL = 'https://localhost:8443/api/';


@Injectable()
export class LicenseService {

    
    constructor(private http: HttpClient) { }

    getLicensesOfUser(userName:string){
        let url = BASE_URL + "licenses/" + userName;
        return this.http.get(url)
    }
    
    canceltAtEndLicense(serial:string,productName:string){
      let url = BASE_URL + "licenses/cancelAtEnd/"+serial+"/"+ productName;
      return this.http.put(url,null)
    }



    updateLicense(license:License){
      let url = BASE_URL + "licenses/update/";
      return this.http.put(url,license)
    }


    getOneLicense(serial:string,productName:string){
      let url = BASE_URL + "licenses/" + serial+"/"+productName;
      return this.http.get(url)
    }

    getLicensesOfUserAndProduct(userName:string,productName:string){
      let url = BASE_URL + "licenses/user/"+userName+"/product/"+productName;
      return this.http.get(url)
    }

}