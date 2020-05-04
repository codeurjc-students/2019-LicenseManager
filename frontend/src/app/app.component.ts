import { Component, ChangeDetectorRef, AfterViewInit, ViewChild, TemplateRef, OnDestroy, ElementRef, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { TdTabSelectBase } from '@covalent/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { UserProfileService } from './userProfile/userProfile.service';
import { AppService } from './app.service';
import { LoginService } from './login/login.service';

declare var Stripe: any;
declare var publicStripeKeyEnv:string;
@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
})

export class AppComponent{
    appName:string;
    mode: string;
    selected:string;
    public environment = environment.environment;

    
    constructor(private router:Router,private appService:AppService, private loginService:LoginService) {
        if (environment.production) {
            this.mode = "Production";
        } else {
            this.mode = "Development";
        }
        this.selected="PRODUCTS";
        
        this.appService.getAppName().subscribe(
            (name:any) => {
                this.appName=name.response;
                this.loginService.resetPages();
                if(this.loginService.isLogged){
                    this.loginService.addPages(this.loginService.isAdmin)
                }
            },
            error=> console.log(error),
        )
        

    }



    manageLinks(route:string){
        this.selected=route;
        switch(route){
            case this.appName:{
                this.selected="PRODUCTS";
                this.router.navigate(["/"]);
                break;
            }
            case 'ADMIN':{
                this.router.navigate(["/admin/dashboard"]);
                break;
            }
            case 'DASHBOARD':{
                this.router.navigate(["user/dashboard"]);
                break;
            }
            case 'PROFILE':{
                this.router.navigate(["user/profile"]);
                break;
            }
            case 'PRODUCTS':{
                this.router.navigate(["/"]);
                break;
            }
            
        }
      }

      

    
}
