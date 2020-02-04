import { OnInit, Component, ViewChild, TemplateRef } from '@angular/core';
import { License } from '../licenses/license.model';
import { LoginService } from '../login/login.service';
import { LicenseService } from '../licenses/license.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { MatDialogRef, MatDialog } from '@angular/material';

@Component({
    selector: 'app-userDashboard',
    templateUrl: './userDashboard.component.html',
  })
export class UserDashboardComponent implements OnInit{
    @ViewChild('searchProductDialog',{static:false}) searchDialog: TemplateRef<any>;
    dialogRef: MatDialogRef<any, any>;

    userName:string;
    activeLicenses:License[];
    

    constructor(public dialog: MatDialog,public licenseServ:LicenseService, private activeRoute: ActivatedRoute, public loginService:LoginService){}


    ngOnInit(): void {
        this.activeRoute.paramMap.subscribe((params: ParamMap) => {
            this.userName = this.loginService.user.name;
            this.getLicenses();
        });
    }


    getLicenses(){
        this.licenseServ.getActiveLicensesOfUser(this.userName).subscribe(
            lics => this.activeLicenses = lics.content,
            error => console.log(error)
        ); 
    }

    








    
}