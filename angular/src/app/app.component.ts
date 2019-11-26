import { Component, ChangeDetectorRef, AfterViewInit, ViewChild, TemplateRef } from '@angular/core';
import { environment } from 'src/environments/environment';
import { TdTabSelectBase } from '@covalent/core';
import { Router } from '@angular/router';


@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
})

export class AppComponent {
    mode: string;

    constructor(private router: Router) {
        if (environment.production) {
            this.mode = "Production";
        } else {
            this.mode = "Development";
        }
    }

    manageLinks(route:string){
        switch(route){
            case 'adminProduct':{
                this.router.navigate(["/admin/product/sw"]);
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
