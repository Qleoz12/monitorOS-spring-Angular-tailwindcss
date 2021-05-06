import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-doughnut-chart',
  templateUrl: './my-doughnut-chart.component.html',
  styleUrls: ['./my-doughnut-chart.component.sass']
})
export class MyDoughnutChartComponent implements OnInit
{
  @Input() labels:string[]=[];
  @Input() data:number[]=[];

  // public doughnutChartLabels = ['Sales Q1', 'Sales Q2', 'Sales Q3', 'Sales Q4'];
  // public doughnutChartData = [120, 150, 180, 90];
  public doughnutChartType = 'doughnut';
  constructor() { }

  ngOnInit(): void {
  }

}
