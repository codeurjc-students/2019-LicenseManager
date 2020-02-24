import { Component, ChangeDetectorRef, AfterViewInit, ViewChild, TemplateRef, OnDestroy, ElementRef, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { TdTabSelectBase } from '@covalent/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { UserProfileService } from './userProfile/userProfile.service';

declare var Stripe: any;
declare var publicStripeKeyEnv:string;
@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
})

export class AppComponent{

    mode: string;
    pages:Map<String,String>;
    public environment = environment.environment;

    
    constructor(private router: Router,private userProfileServ:UserProfileService) {
        if (environment.production) {
            this.mode = "Production";
        } else {
            this.mode = "Development";
        }
        this.pages = new Map();
    }

      

    
}
