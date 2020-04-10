import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { LoginService } from '../login/login.service';
import { Product } from '../product/product.model';
import { ActivatedRoute, ParamMap } from '@angular/router';

const BASE_URL = '/api/users/';


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


    addCardStripeElements(user:string,tokenId:string){
        return this.http.post(BASE_URL +user +'/addCard/' + tokenId,null);
    }

    getDefaultCard(user:string){
        return this.http.get(BASE_URL +user +'/default-card/');
    }

    setDefaultCard(user:string,pmId:string){
        return this.http.put(BASE_URL +user +'/default-card/'+pmId,null);
    }

    addSubscriptionToProduct(product:Product,typeSubs:string,userName:string, automaticRenewal:boolean, pmId:string){
        if(pmId==null){
            pmId="default";
        }
        return this.http.put(BASE_URL+ userName + "/products/" + product.name+"/" + typeSubs  +"/addSubscription/renewal/"+automaticRenewal+"/paymentMethods/"+pmId,product)
        
    }

    addFreeTrial(product:Product,userName:string,days:number, token:string){
        return this.http.put(BASE_URL + userName + "/products/" + product.name + "/addTrial/cards/" + token,product) ;
    }


    //Methods for one-time payment
    pay(userName:string,product:Product, tokenId:string){
        return this.http.post(BASE_URL+ userName + "/paymentIntent/"+tokenId,product)
    }

    confirmPay(userName:string,product:Product,id:string){
        return this.http.post(BASE_URL+ userName + "/confirm/" + id + "/products/"+product.name,{})

    }



    private handleError(error: any) {
        console.error(error);
        return Observable.throw("Server error (" + error.status + "): " + error.text())
    }
}