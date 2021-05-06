import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { ChartsModule } from 'ng2-charts';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MyDoughnutChartComponent } from './components/charts/my-doughnut-chart/my-doughnut-chart.component';
import { HomeComponent } from './components/home/home.component';
import { MemoryComponent } from './components/hard/memory/memory.component';
import { MyLineChartComponent } from './components/charts/my-line-chart/my-line-chart.component';
import { CpuComponent } from './components/hard/cpu/cpu.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MyDoughnutChartComponent,
    MemoryComponent,
    MyLineChartComponent,
    CpuComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    HttpClientModule,
    FormsModule,
    ChartsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
