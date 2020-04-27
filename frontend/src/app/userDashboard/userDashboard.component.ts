import { OnInit, Component, ViewChild, TemplateRef } from '@angular/core';
import { License } from '../licenses/license.model';
import { LoginService } from '../login/login.service';
import { LicenseService } from '../licenses/license.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { MatDialogRef, MatDialog, MatSnackBarConfig, MatSnackBar } from '@angular/material';
import { DatePipe } from '@angular/common';
import { DialogService } from '../dialogs/dialog.service';
import { Product } from '../product/product.model';
import { DomSanitizer } from '@angular/platform-browser';
import { UserProfileService } from '../userProfile/userProfile.service';

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
    numberOfElements = 5;
    
    pageActual:number;
    fileurl;
    fileName:string;

    conf:MatSnackBarConfig= new MatSnackBarConfig();


    constructor(private snackbar:MatSnackBar,private sanitizer: DomSanitizer,private dialogService:DialogService,private licenseService:LicenseService,private datepipe: DatePipe,private router:Router,public dialog: MatDialog,public licenseServ:LicenseService, private activeRoute: ActivatedRoute, public loginService:LoginService, public userProfileService:UserProfileService){}


    ngOnInit(): void {
        this.pageActual=1;
        this.loading=false;
        this.activeRoute.paramMap.subscribe((params: ParamMap) => {
            this.userName = this.loginService.user.name;
            this.getLicenses();
        });
    }


    getLicenses(){
        this.licenseServ.getLicensesOfUser(this.userName).subscribe(
            (lics:any) => {this.activeLicenses = lics;},
            error => console.log(error)
        ); 
    }

    manageLink(productName:string){
        this.router.navigate(["products/",productName]);
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
        this.dialogService.openConfirmDialog(msg,false,true)
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

    downloadDialog(license:License){
        this.fileName="license-"+license.product.name+".txt";
        const blob = new Blob([license.licenseString], { type: 'application/octet-stream' });
        this.fileurl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob));
        this.dialogService.openConfirmDialog("You are going to download the License File of " + license.product.name + "'s license with serial: " + license.serial + ".\n This file cannot be modified. If done, it will NOT work.",false,false).afterClosed().subscribe(
            res => {
                if(res){
                    let element: HTMLElement = document.getElementById('download') as HTMLElement;
                    element.click();
                }
            }
        )
    }

    editPaymentMethod(subsId:string){
        this.dialogService.openCardSubscriptionSelectDialog(this.userName,"Cards available",subsId).afterClosed().subscribe(
            res => {
                if(res[0]){
                    this.userProfileService.setPaymentMethodOfSubs(subsId,this.userName,res[1]).subscribe(
                        (res:any) => {this.conf.duration=3000,this.snackbar.open("Method of payment changed succesfully","X",this.conf)},
                        error => console.log(error) 
                    )
                }
            }
        )
    }

    
}