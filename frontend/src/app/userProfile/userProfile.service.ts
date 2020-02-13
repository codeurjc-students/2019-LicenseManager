import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { LoginService } from '../login/login.service';
import { Product } from '../product/product.model';

const BASE_URL = 'http://localhost:8080/api/user/';


@Injectable()
export class UserProfileService {

    
    constructor(private http: HttpClient, private loginServ:LoginService) { 
        
    }


    addCardStripeElements(tokenId:string){
        return this.http.post(BASE_URL + 'addCard/' + tokenId,null).pipe(
            map(response => console.log(response)),
        );
    }



    addSubscriptionToProduct(product:Product,typeSubs:string,userName:string, automaticRenewal:boolean){
        return this.http.put(BASE_URL+product.name+"/" + typeSubs + "/" + userName +"/addSubscription/renewal/"+automaticRenewal,product)
        
    }

    //No sé si debería ir aquí
    buyProduct(token:string,product:Product, userName:string){
        return this.http.put(BASE_URL+userName+"/buy/" + product.name + "/" + token ,product)
    }

    test(){
        console.log("SE");
    }

    private handleError(error: any) {
        console.error(error);
        return Observable.throw("Server error (" + error.status + "): " + error.text())
    }
}