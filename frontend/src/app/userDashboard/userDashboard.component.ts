import { OnInit, Component, ViewChild, TemplateRef } from '@angular/core';
import { License } from '../licenses/license.model';
import { LoginService } from '../login/login.service';
import { LicenseService } from '../licenses/license.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { MatDialogRef, MatDialog } from '@angular/material';
import { DatePipe } from '@angular/common';
import { DialogService } from '../dialogs/dialog.service';
import { Product } from '../product/product.model';

@Component({
    selector: 'app-userDashboard',
    templateUrl: './userDashboard.component.html',
    styleUrls: ['./userDashboard.component.css']
  })
export class UserDashboardComponent implements OnInit{
    @ViewChild('searchProductDialog',{static:false}) searchDialog: TemplateRef<any>;
    dialogRef: MatDialogRef<any, any>;

    userName:string;
    activeLicenses:License[];
    loading:boolean;
    

    constructor(private dialogService:DialogService,private licenseService:LicenseService,private datepipe: DatePipe,private router:Router,public dialog: MatDialog,public licenseServ:LicenseService, private activeRoute: ActivatedRoute, public loginService:LoginService){}


    ngOnInit(): void {
        this.loading=false;
        this.activeRoute.paramMap.subscribe((params: ParamMap) => {
            this.userName = this.loginService.user.name;
            this.getLicenses();
        });
    }


    getLicenses(){
        this.licenseServ.getLicensesOfUser(this.userName).subscribe(
            (lics:any) => {this.activeLicenses = lics.content;},
            error => console.log(error)
        ); 
    }

    manageLink(productName:string){
        this.router.navigate(["product/",productName]);
    }

    goToStats(productName:string, serial:string){
        this.router.navigate(["user/dashboard/"+productName+"/statistics/"+serial]);
    }

    cancelAtEnd(license:License){
        let msg;
        if(license.cancelAtEnd){
            msg="Do you want to activate the automatic renewal (at the end date) of your license of " + license.product.name + "?";
        }else{
            msg="Do you want to deactivate the automatic renewal (at the end date) of your license of " + license.product.name + "? It will still be valid until the end date."
        }
        this.dialogService.openConfirmDialog(msg,false)
      .afterClosed().subscribe(
          suc=>{
              if(suc[0]){
                  this.loading=true;
                this.licenseService.canceltAtEndLicense(license.serial,license.product.name).subscribe(
                ans=>{this.getLicenses();this.loading=false;},
                error=>{console.log(error);this.loading=false;}
                );
          }
        },
      );
    }

    
}