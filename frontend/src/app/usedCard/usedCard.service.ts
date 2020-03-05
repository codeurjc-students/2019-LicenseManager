import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

const BASE_URL = 'https://localhost:8443/api/usedcard/';


@Injectable({
    providedIn:'root'
})
export class UsedCardService {

    constructor(private http: HttpClient) { }

    checkCard(last4:number,expMonth:number,expYear:number, product:string){
        return this.http.get(BASE_URL+ "checkUsed/" + last4 + "/" + expMonth + "/" + expYear + "/product/" + product);
    }

    postUsedCard(last4:number,expMonth:number,expYear:number, product:string){
        let a = {};
        return this.http.post(BASE_URL + last4 + "/" + expMonth + "/" + expYear + "/product/" + product,a);

    }

}