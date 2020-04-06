import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import 'rxjs/Rx';

const URL = 'https://localhost:8443/api';

export interface User {
    id?: number;
    name: string;
    roles: string[];
    authdata: string;
    userStripeId:string;
    email:string;
}

@Injectable()
export class LoginService {

    isLogged = false;
    isAdmin = false;
    user: User;
    auth: string;
    pages:String[]=[];

    constructor(private http: HttpClient) {
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
                    this.addPages(this.isAdmin);
                }
                
                return user;
            }));
           
    }

    logOut() {
        
        return this.http.get(URL + '/logOut').pipe(
            map(response => {
                this.resetPages();
                this.removeCurrentUser();
                return response;
            }),
        );
    }

    private setCurrentUser(user: User) {
        this.isLogged = true;
        this.user = user;
        this.isAdmin = this.user.roles.indexOf('ROLE_ADMIN') !== -1;
    }


    getUsers(){
        let url = URL + "/users/";
      return this.http.get(url)
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


    getUserLogged(){
        return this.user;
    }

    getUserLoggedBack(){
        let url = URL + "/getUserLogged/";
        return this.http.get(url);
    }

    resetPages(){
        this.pages=[];
        this.pages.push("PRODUCTS");
    }

    addPages(admin:boolean){
        this.pages.push("DASHBOARD");
        this.pages.push("PROFILE");
                     
        if(admin){
            this.pages.push("ADMIN");
        }
    }
}