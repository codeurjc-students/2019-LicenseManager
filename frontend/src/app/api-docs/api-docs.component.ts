import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
    templateUrl: './api-docs.component.html',
  })
  export class ApiDocsComponent implements OnInit {

    constructor(private router:Router,private http: HttpClient) {} 


    ngOnInit(): void {
        let url = "api/appDomain";
        this.http.get<string>(url).subscribe(
            //Not working on development = frontend endpoint is localhost:4200, and swagger ui is under backend endpoint localhost:80/
            (s:any)=>{console.log(s.response);window.location.href=s.response+"/swagger-ui.html"}
        )
    }


  }