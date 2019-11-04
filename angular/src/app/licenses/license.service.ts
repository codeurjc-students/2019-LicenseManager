import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8080/api/';


@Injectable()
export class LicenseService {

    
    constructor(private http: Http) { }

    getActiveLicensesOfUser(userName:string){
        let url = BASE_URL + "licenses/" + userName;
        return this.http.get(url)
			.map(response => response.json())
			.catch(error => this.handleError(error));
    }

    private handleError(error: any) {
        console.error(error);
      return Observable.throw("Server error (" + error.status + "): " + error.text())
  }
}