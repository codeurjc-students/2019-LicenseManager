import { Component, OnInit, Input, SimpleChanges, Output, EventEmitter } from '@angular/core';


@Component({
    selector: 'app-pie-chart',
    templateUrl: './pie-chart.component.html',

  })
  export class PieChartComponent implements OnInit{
    @Input()licenseStats:any[];

    @Input()statsMap:Map<any,any>;
    @Output()setLineChart = new EventEmitter<string>();

    view: any[] = [700, 200];
  
    // options
    gradient: boolean = true;
    showLegend: boolean = true;
    showLabels: boolean = true;
    isDoughnut: boolean = false;



    colorScheme = {
        domain: []
    };



    ngOnInit(): void {
        for(let i = 0; i<this.statsMap.size; i++){
            this.colorScheme.domain.push('#'+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6));
        }
    }

    constructor(){


    }
 
    ngOnChanges(changes: SimpleChanges){
        this.statsMap= changes.statsMap.currentValue;
        let single: any[] = [];

        for(let entry of this.statsMap.entries()){
            if(entry[0]==null){
                entry[0]="Others"
            }
            single.push(
                {
                    "name": entry[0],
                    "value": entry[1]
                }
            )
        }

        Object.assign(this, { single });

    }

    onSelect(data): void {      
        this.setLineChart.emit(data.name);
    }
    
    onActivate(data): void {

     }
    
    onDeactivate(data): void { }

  }