import { Component, OnInit } from '@angular/core';
import { ProcessorIdentifier } from 'src/app/models/cpu.model';
import { CpuService } from 'src/app/services/cpu.services';

@Component({
  selector: 'app-cpu',
  templateUrl: './cpu.component.html',
  styleUrls: ['./cpu.component.sass']
})
export class CpuComponent implements OnInit {

  procesadorInfo!: ProcessorIdentifier;
  constructor(private cpuService:CpuService) { }


  ngOnInit(): void {
    this.load()
  }

  load()
  {

    this.cpuService.getcpu()
    .subscribe(
      res => {

        console.log(res.body.processorIdentifier);
        this.procesadorInfo=res.body.processorIdentifier;
      })
  }
}
