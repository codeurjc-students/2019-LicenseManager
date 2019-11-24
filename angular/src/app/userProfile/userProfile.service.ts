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

    
    constructor(private http: Http, private loginServ:LoginService) { 
        
    }

    addCard(owner:string,cvv:string,number:string,exp_month:string,exp_year:string){
        let params = new Map();
        params.set("owner",owner);
        params.set("cvv",cvv);
        params.set("exp_month",exp_month);
        params.set("exp_year",exp_year);
        params.set("number",number);

        let card = {owner:owner,cvv:cvv,exp_month:exp_month,exp_year:exp_year,number:number};

        
        //let params:string[]= [number,cvv,exp_month,exp_year];

        console.log(card);

        return this.http.post(BASE_URL + 'addCard',card).pipe(
            map(response => console.log(response)),
        );
    }

    
    updateCards(){
        return this.http.put(BASE_URL,this.loginServ.user)
            .map(response => response.json())
		    .catch(error => this.handleError(error));
       
    }


    getCards(){
        return this.http.get(BASE_URL+"stripeCards")
        .map(response => response.json())
        .catch(error => this.handleError(error));
    }

    addSubscriptionToProduct(product:Product,typeSubs:string){
        return this.http.put(BASE_URL+product.name+"/" + typeSubs + "/addSubscription",product)
        .map(response => response.json())
        .catch(error => this.handleError(error));
    }


    private handleError(error: any) {
        console.error(error);
        return Observable.throw("Server error (" + error.status + "): " + error.text())
    }
}