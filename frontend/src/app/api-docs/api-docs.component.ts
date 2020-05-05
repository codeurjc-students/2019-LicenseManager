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
            (s:any)=>{
                if(s.response.charAt(4)=="s"){
                    window.location.href="https://localhost/swagger-ui.html"
                }else{
                    window.location.href="http://localhost/swagger-ui.html"
                }
            }
        )
    }


  }