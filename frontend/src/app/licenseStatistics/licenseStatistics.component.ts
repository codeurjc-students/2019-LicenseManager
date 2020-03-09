import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { License } from '../licenses/license.model';
import { LicenseService } from '../licenses/license.service';

@Component({
    selector: 'app-licenseStats',
    templateUrl: './licenseStatistics.component.html',
    styleUrls: ['./licenseStatistics.component.css']
  })
export class LicenseStatisticsComponent implements OnInit{

    productName:string;
    license:License;
    constructor(private activeRoute: ActivatedRoute, private licenseServ:LicenseService){}

    

    ngOnInit(): void {
        this.activeRoute.paramMap.subscribe((params: ParamMap) => {
            this.productName = params.get('name');
            let serial = params.get('serial');
            this.licenseServ.getOneLicense(serial,this.productName).subscribe(
                l=>{ this.license=l;console.log(l)},
                error => console.log(error)
            )
        });
    }
}