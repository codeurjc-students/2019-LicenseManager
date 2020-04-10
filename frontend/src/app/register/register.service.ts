import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../login/login.service';


    const URL = '/api';

@Injectable()
export class RegisterService {
    constructor(private http:HttpClient){}



    register(user:string,pass1:string,pass2:string,email:string){
        const headers = new HttpHeaders({
            Authorization: 'Basic ' + user + ':' + pass1,
            'X-Requested-With': 'XMLHttpRequest',
        });

        let url = URL + "/register/" + user + "/"+ pass1 + "/" + pass2 + "/" + email;

        return this.http.post<User>(url, { headers })

    }

}