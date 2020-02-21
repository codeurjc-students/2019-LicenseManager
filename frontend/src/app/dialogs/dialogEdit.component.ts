import { Component, Inject } from '@angular/core';
import { License } from '../licenses/license.model';
import { LicenseService } from '../licenses/license.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { User, LoginService } from '../login/login.service';

@Component({
    selector: 'app-dialogEdit',
    templateUrl: './dialogEdit.component.html',
  })
  export class DialogEditComponent  {

    license:License;
    users:User[];

    constructor( @Inject(MAT_DIALOG_DATA) public data:any, public  licenseService:LicenseService, private dialogRef:MatDialogRef<DialogEditComponent>, private loginServ:LoginService){
        this.license=data.license;
        this.getUsers();
    }


    saveLicense(){
        this.licenseService.updateLicense(this.license).subscribe(
            ans=>{this.dialogRef.close();},
            error=> console.log(error)
        );
    }

    close(){
        this.dialogRef.close();
    }

    getUsers(){
        this.loginServ.getUsers().subscribe(
            (users:any)=>this.users=users.content,
            error=>console.log(error),
        );
    }

  }