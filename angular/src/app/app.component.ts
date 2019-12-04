import { Component, ChangeDetectorRef, AfterViewInit, ViewChild, TemplateRef, OnDestroy, ElementRef, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { TdTabSelectBase } from '@covalent/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { UserProfileService } from './userProfile/userProfile.service';

declare var Stripe: any;
@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
})

export class AppComponent {
    mode: string;

    
    
    constructor(private router: Router,private userProfileServ:UserProfileService) {
        if (environment.production) {
            this.mode = "Production";
        } else {
            this.mode = "Development";
        }
    }

      
    manageLinks(route:string){
        switch(route){
            case 'adminDashboard':{
                this.router.navigate(["/admin/dashboard"]);
                break;
            }
            case 'userDashboard':{
                this.router.navigate(["user/dashboard"]);
                break;
            }
            case 'userProfile':{
                this.router.navigate(["user/profile"]);
                break;
            }
            
        }
    }

    
}
