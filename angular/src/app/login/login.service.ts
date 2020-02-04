import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import 'rxjs/Rx';
import { Http } from '@angular/http';
import { StripeCard } from '../stripe/stripeCard.model';

const URL = 'http://localhost:8080/api';

export interface User {
    id?: number;
    name: string;
    roles: string[];
    authdata: string;
    userStripeId:string;
    stripeCards:StripeCard[];
}

@Injectable()
export class LoginService {

    isLogged = false;
    isAdmin = false;
    user: User;
    auth: string;

    constructor(private http: HttpClient,private http2: Http) {
        let user = JSON.parse(localStorage.getItem('currentUser'));
        if (user) {
            this.setCurrentUser(user);
        }
    }

    logIn(user: string, pass: string) {

        let auth = window.btoa(user + ':' + pass);

        const headers = new HttpHeaders({
            Authorization: 'Basic ' + auth,
            'X-Requested-With': 'XMLHttpRequest',
        });

        return this.http.get<User>(URL+'/logIn', { headers })
            .pipe(map(user => {

                if (user) {
                    this.setCurrentUser(user);
                    user.authdata = auth;
                    localStorage.setItem('currentUser', JSON.stringify(user));
                    console.log("adasdsa");
                }
                return user;
            }));
           
    }

    logOut() {
        
        return this.http.get(URL + '/logOut').pipe(
            map(response => {
                this.removeCurrentUser();
                return response;
            }),
        );
    }

    private setCurrentUser(user: User) {
        console.log(localStorage.length);
        this.isLogged = true;
        this.user = user;
        this.isAdmin = this.user.roles.indexOf('ROLE_ADMIN') !== -1;
    }


    getUsers(){
        let url = URL + "/users/";
      return this.http2.get(url)
    .map(response => response.json())
    .catch(error => this.handleError(error));
    }

    
    removeCurrentUser() {
        localStorage.removeItem('currentUser');
        this.isLogged = false;
        this.isAdmin = false;
    }


    private handleError(error: any) {
        console.error(error);
        return Observable.throw("Server error (" + error.status + "): " + error.text())
    }

   /* getUserLogged(){
        let url = URL + "/getUserLogged/";
      return this.http2.get(url);
    }
    ERA PARA HACER PRUEBAS CON EL BACK*/ 

    getUserLogged(){
        return this.user;
    }
}