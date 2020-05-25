import { Component, OnInit } from '@angular/core';
import { ProductService } from '../product/product.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Product } from '../product/product.model';
import { License } from '../licenses/license.model';
import { KeyValue } from '@angular/common';
import { HttpClient } from '@angular/common/http';

export interface Stats{
    nUsage:number;
    usages:Date[];
	usagePerTime:Map<String,number>;
}

@Component({
    selector: 'product-statistics',
    templateUrl: './product-statistics.component.html',
    styleUrls: ['./product-statistics.component.css']
  })
  export class ProductStatisticsComponent implements OnInit{

    product:Product;
    licenses:License[];


    numberOfElementsLicense = 10;    
    numberOfElementsOwner = 10;    

    pageActualLicense:number = 1;
    pageActualOwner:number = 1;

    statsByOwnerMap: Map<string,number> = new Map();
    statsByLicenseMap: Map<string,number> = new Map();

    statListMapLicense: Map<string,Stats> = new Map();

      
    n:number;

    constructor(private http:HttpClient,private router:Router,private activeRoute: ActivatedRoute,private prodService:ProductService){}


    ngOnInit(): void {
        this.activeRoute.paramMap.subscribe((params: ParamMap) => {
            let productName = params.get('name');
            this.prodService.getProduct(productName).subscribe(
                (pr:any)=>{this.product=pr;
                    this.prodService.getLicensesByProduct(this.product.name).subscribe(
                        (lic:any)=>{this.licenses=lic;        this.licenses = this.licenses.sort((a, b) => b.nUsage - a.nUsage);
                            this.mapByLicense();this.mapByOwner()},
                        error=>console.log(error)
                    )},
                error=> console.log(error)
            )
        });
    }

    mapByLicense(){
        this.licenses.forEach(
            function(value) {
                this.n=0;
                value.licenseStats.forEach(element => {
                    this.n=element.nUsage + this.n;
                });
                let x = this.statsByLicenseMap.get(value.serial);
                if(x!=null){
                    this.statsByLicenseMap.set(value.serial,x+this.n);
                }else{
                    this.statsByLicenseMap.set(value.serial,this.n);
                }
            }.bind(this)
        );

        
    }

    mapByOwner(){
        this.licenses.forEach(
            function(value) {
                
                let p = this.statsByOwnerMap.get(value.owner);
                if(p==null){
                    this.statsByOwnerMap.set(value.owner,0);
                }
                
                value.licenseStats.forEach(element => {
                    let x = this.statsByOwnerMap.get(value.owner);
                    if(x!=null){
                        this.statsByOwnerMap.set(value.owner,x+element.nUsage);
                    }else{
                        this.statsByOwnerMap.set(value.owner,element.nUsage);
                    }
                });
                
            }.bind(this)
        );
    }


    setLineChart($event: any){
        this.router.navigateByUrl("user/products/"+this.product.name+"/statistics/" + $event)
    }


    
    resetIndexPage($event:any){
        this.pageActualLicense=1;
        this.pageActualOwner=1;

        this.numberOfElementsLicense=5;
        this.numberOfElementsOwner=5;
    }

    // Order by ascending property value
    valueDescOrder = (a: KeyValue<string,number>, b: KeyValue<string,number>): number => {
        return a.value > b.value ? -1 : (b.value > a.value ? 1 : 0);
    }

    goToStats(productName:string, serial:string){
        this.router.navigate(["user/products/"+productName+"/statistics/"+serial]);
    }

  }