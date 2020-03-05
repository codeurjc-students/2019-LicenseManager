import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { LoginService } from '../login/login.service';
import { Product } from '../product/product.model';
import { ActivatedRoute, ParamMap } from '@angular/router';

const BASE_URL = 'https://localhost:8443/api/user/';


@Injectable()
export class UserProfileService {

    constructor(private http: HttpClient, private loginServ:LoginService) { 
    }

    getUserCardsStripe(user:string){
        return this.http.get(BASE_URL + user + "/cards");
    }

    deleteStripeCard(user:string, id:string){
        return this.http.delete(BASE_URL + user + "/card/" + id);
    }


    addCardStripeElements(tokenId:string){
        return this.http.post(BASE_URL + 'addCard/' + tokenId,null);
    }


    addSubscriptionToProduct(product:Product,typeSubs:string,userName:string, automaticRenewal:boolean){
        return this.http.put(BASE_URL+product.name+"/" + typeSubs + "/" + userName +"/addSubscription/renewal/"+automaticRenewal,product)
        
    }

    addFreeTrial(product:Product,userName:string,days:number){
        return this.http.put(BASE_URL + product.name + "/" +userName+ "/addTrial/"+days,product);
    }


    buyProduct(token:string,product:Product, userName:string){
        return this.http.put(BASE_URL+userName+"/buy/" + product.name + "/" + token ,product)
    }



    private handleError(error: any) {
        console.error(error);
        return Observable.throw("Server error (" + error.status + "): " + error.text())
    }
}