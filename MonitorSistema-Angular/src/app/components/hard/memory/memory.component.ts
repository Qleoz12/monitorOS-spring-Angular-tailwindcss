import { Component, OnInit, ViewChild } from '@angular/core';
import { interval } from 'rxjs/observable/interval';
import { Memory } from 'src/app/models/memory.model';
import { MemoryService } from 'src/app/services/Memory.services';
import { BaseChartDirective } from 'ng2-charts'
import { ChartOptions } from 'chart.js';


@Component({
  selector: 'app-memory',
  templateUrl: './memory.component.html',
  styleUrls: ['./memory.component.sass']
})
export class MemoryComponent implements OnInit {


  public labels = ['' ];
  public data = [0];
  current: any;
  generallabel="consumo de memoria"
  options: any;

  @ViewChild('mylinechart') private chartComponent: any;
  @ViewChild('app-my-line-chart') private linechartComponent: any;


  constructor(private memoryService:MemoryService) { }

  ngOnInit(): void {
    interval(1000).subscribe(x => this.load())
  }

  load()
  {
    console.log(this.chartComponent)
    this.memoryService.getMemory()
    .subscribe(
      res => {
        console.log(res);
        console.log(this.chartComponent)
        console.log(this.linechartComponent)
        this.current=res.body;
        if(!this.options)
        {
          this.options = {
            scales : {
                y : {
                    min: 0,
                    max: this.current.total,
                    stacked: false,
                    beginAtZero: true,
                    suggestedMin: 0,
                    suggestedMax: this.current.total
                },
                x: {
                  beginAtZero: true
                }
              }}
        }
        console.log(this.options)
        this.labels.push("")
        this.data.push(this.current.total-this.current.available)
      },
      err => {
        console.log(err);
      })
  }
}


