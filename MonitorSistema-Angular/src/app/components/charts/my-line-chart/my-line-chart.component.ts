import { Component, Input, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-my-line-chart',
  templateUrl: './my-line-chart.component.html',
  styleUrls: ['./my-line-chart.component.sass']
})
export class MyLineChartComponent implements OnInit {
  @Input() labels:string[]=[];
  @Input() datainput:number[]=[];
  datasets: ChartDataSets[]=[]
  @Input() generallabel:string ="";
  @Input() chartOptions: any;

  constructor() { }

  ngOnInit(): void {
    this.datasets=[{
      label: this.generallabel,
      data: this.datainput,
      fill: true
    }]
  }

}
