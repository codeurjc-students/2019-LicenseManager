import { Component, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { License } from '../licenses/license.model';
import { LicenseService } from '../licenses/license.service';
import { DatePipe } from '@angular/common';
import { PieChartComponent } from '../charts/pie-chart.component';

export interface Stats{
    nUsage:number;
    usages:Date[];
	usagePerTime:Map<String,number>;
}


@Component({
    selector: 'app-licenseStats',
    templateUrl: './licenseStatistics.component.html',
    styleUrls: ['./licenseStatistics.component.css']
  })
export class LicenseStatisticsComponent implements OnInit{

    numberOfElementsIP = 10;    
    pageActualIP:number = 1;

    numberOfElementsName = 10;    
    pageActualName:number = 1;

    numberOfElementsIPName = 10;    
    pageActualIPName:number = 1;

    productName:string;
    license:License;

    loading:boolean = false;

    licensesByNameMap: Map<String,Number> = new Map();
    licensesByIpMap: Map<String,Number> = new Map();
    licensesByIpAndNameMap: Map<String,Number> = new Map();

    statListMapIP: Map<String,Stats> = new Map();
    statListMapName: Map<String,Stats> = new Map();
    statListMapIPName: Map<String,Stats> = new Map();

    selected:string = "";
    lineChartMap:Map<string,number> = new Map();

    periodSelected:number;

    trigger:boolean = true;

    
    constructor(private activeRoute: ActivatedRoute, private licenseServ:LicenseService, private datePipipe:DatePipe){}

    

    ngOnInit(): void {
        this.activeRoute.paramMap.subscribe((params: ParamMap) => {
            this.productName = params.get('name');
            let serial = params.get('serial');
            this.licenseServ.getOneLicense(serial,this.productName).subscribe(
                (l:any)=>{ this.license=l;this.periodSelected=l.period;  this.mapByIp(); this.mapByName(); this.listByName(); this.listIP(); this.listByIPAndName();this.mapByNameIP()
                },
                error => console.log(error)
            )
        });
    }

    rechargeData(){
        this.statListMapIP.clear(); this.statListMapIPName.clear(); this.statListMapName.clear(); this.licensesByIpAndNameMap.clear(); this.licensesByIpMap.clear(); this.licensesByNameMap.clear();
        this.mapByIp(); this.mapByName(); this.listByName(); this.listIP(); this.listByIPAndName();this.mapByNameIP()

    }

    //Method that provides the info of the list by Ip
    listIP(){
        this.license.licenseStats.forEach( function(value) {
            if(value.period==this.periodSelected){
                let obj = this.statListMapIP.get(value.ip);
                let stat:Stats;
                if(obj==null){
                    let map=this.mixUsagePerTimes(Object.entries(value.usagePerTime),null);
                    stat= {nUsage:value.nUsage, usages: value.usages, usagePerTime: map };
                }else{
                    let aux = value.usages.concat(obj.usages).sort();
                    let map:Map<String,number> = this.mixUsagePerTimes(Object.entries(value.usagePerTime),obj.usagePerTime.entries());
                    stat = {nUsage:value.nUsage + obj.nUsage, usages: aux,  usagePerTime: map};
                }        
                this.statListMapIP.set(value.ip,stat);
            }

        }.bind(this));

        
    }

    mixUsagePerTimes(entries1:any,entries2:any){
        let map = new Map();
        for(let i =0; i<entries1.length;i++){
            map.set(entries1[i][0],entries1[i][1]);
        }
        //If 2 is null, this method just passes the entries1 to a Map
        if(entries2!=null){
            let x: any = entries2.next().value;
            while(x!=null){
                if(map.has(x[0])){
                    map.set(x[0],x[1] + map.get(x[0]));
                }else{
                    map.set(x[0],x[1]);
                }
                x = entries2.next().value;
            }
        }
        return map;
    }

    //Method that provides the info to the chart
    mapByIp(){
        this.license.licenseStats.forEach(
            function(value) {
                if(value.period==this.periodSelected){

                    let x = this.licensesByIpMap.get(value.ip);
                    if(x!=null){
                        this.licensesByIpMap.set(value.ip,x+value.nUsage);
                    }else{
                        this.licensesByIpMap.set(value.ip,value.nUsage);
                    }
                }
            }.bind(this)
        );
    }

     //Method that provides the info of the list by UserName
    listByName(){
        this.license.licenseStats.forEach( function(value) {
            if(value.period==this.periodSelected){

                if(value.userName==null){
                    value.userName= "Others";
                }
                let obj = this.statListMapName.get(value.userName);
                let stat:Stats;
                if(obj==null){
                    let map=this.mixUsagePerTimes(Object.entries(value.usagePerTime),null);
                    stat= {nUsage:value.nUsage, usages: value.usages , usagePerTime: map};
                }else{
                    let map:Map<String,number> = this.mixUsagePerTimes(Object.entries(value.usagePerTime),obj.usagePerTime.entries());
                    let aux = value.usages.concat(obj.usages).sort();
                    stat = {nUsage:value.nUsage + obj.nUsage, usages: aux , usagePerTime:map};
                }       
                this.statListMapName.set(value.userName,stat);
            }

        }.bind(this));
    }


    //Method that provides the info to the chart
    mapByName(){
        this.license.licenseStats.forEach(
            function(value) {
                if(value.period==this.periodSelected){

                    let x = this.licensesByNameMap.get(value.userName);
                    if(x!=null){
                        this.licensesByNameMap.set(value.userName,x+value.nUsage);
                    }else{
                        this.licensesByNameMap.set(value.userName,value.nUsage);
                    }
                }
            }.bind(this)
        );

    }

    listByIPAndName(){
        this.license.licenseStats.forEach( function(value) {
            if(value.period==this.periodSelected){

                if(value.userName==null){
                    value.userName= "Others";
                }
                let obj = this.statListMapIPName.get(value.userName+"%"+value.ip);
                let stat:Stats;
                if(obj==null){
                    let map=this.mixUsagePerTimes(Object.entries(value.usagePerTime),null);
                    stat= {nUsage:value.nUsage, usages: value.usages , usagePerTime: map};
                }else{
                    console.log("no debería imprimirse esto");
                    let map:Map<String,number> = this.mixUsagePerTimes(Object.entries(value.usagePerTime),obj.usagePerTime.entries());
                    let aux = value.usages.concat(obj.usages).sort();
                    stat = {nUsage:value.nUsage + obj.nUsage, usages: aux , usagePerTime:map};
                }       
                this.statListMapIPName.set(value.userName+"%"+value.ip,stat);
            }

        }.bind(this));
    }

    
    mapByNameIP(){
        this.license.licenseStats.forEach(
            function(value) {
                if(value.period==this.periodSelected){

                    let x = this.licensesByIpAndNameMap.get(value.userName+"%"+value.ip);
                    if(x!=null){
                        this.licensesByIpAndNameMap.set(value.userName+"%"+value.ip,x+value.nUsage);
                    }else{
                        this.licensesByIpAndNameMap.set(value.userName+"%"+value.ip,value.nUsage);
                    }
                }
            }.bind(this)
        );
    }


    setLineChart($event: any){
        this.loading = true;
        this.lineChartMap = new Map();
        this.selected=$event;
        let x;
        if($event.includes("%")){
            x= this.statListMapIPName.get($event);
        }else if($event.match("[0-9.:]*")!=""){
            x = this.statListMapIP.get($event);
        }else{
            x = this.statListMapName.get($event);
        }
        this.lineChartMap = x.usagePerTime;
        this.loading = false;
    }

    resetIndexPage($event:any){
        this.pageActualIP=1;
        this.pageActualIPName=1;
        this.pageActualName=1;

        this.numberOfElementsIP=5;
        this.numberOfElementsIPName=5;
        this.numberOfElementsName=5;
    }

    changePeriod($event:any){
        this.periodSelected=this.license.period - $event.value;
        this.trigger=!this.trigger;
        this.rechargeData();
    }


}