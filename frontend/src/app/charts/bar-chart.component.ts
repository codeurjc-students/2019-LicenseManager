import { Component, Input, SimpleChanges, OnInit, Output, EventEmitter, HostListener } from '@angular/core';

@Component({
    selector: 'bar-chart-component',
    templateUrl: './bar-chart.component.html',
  })
  export class BarChartComponent implements OnInit{
    @Input()statsMap:Map<String,number>;


    single: any[];
    multi: any[];
    view: any;
  
    // options
    showXAxis = true;
    showYAxis = true;
    gradient = false;
    showLegend = false;
    showXAxisLabel = true;
    xAxisLabel = 'Date (MM/dd/yyyy)';
    showYAxisLabel = true;
    yAxisLabel = 'Usages';
  
    colorScheme = {
        domain: []
    };

    
    ngOnInit(): void {
        this.view = [window.innerWidth-50, 300];
       // for(let i = 0; i<this.statsMap.size; i++){
            this.colorScheme.domain.push("#3F51B5");
       // }

       //this.statsMap = changes.statsMap.currentValue;
      
    }

    @HostListener('window:resize', ['$event'])
    onResize(event) {
    this.view = [window.innerWidth - 50, 300];
    }
  
    constructor() {

    }

    ngOnChanges(changes: SimpleChanges){
        let single: any[] = [];
        var mapAsc = new Map([...this.statsMap.entries()].sort());


        for(let entry of mapAsc.entries()){
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
    }
  
    onActivate(data): void {
    }
  
    onDeactivate(data): void {
    }
  }