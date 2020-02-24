import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

const BASE_URL = 'http://localhost:8080/api/';


@Injectable()
export class AppService{

    public publicApiKey:string;
    constructor(private http: HttpClient) { 
        
        this.getPublicStripeKey().subscribe(
            (k:any)=> {this.publicApiKey=k.key},
        )
    }
    

    getPublicStripeKey(){
        let url = BASE_URL + "keys/stripe/public";
        return this.http.get<String>(url)
    }
}