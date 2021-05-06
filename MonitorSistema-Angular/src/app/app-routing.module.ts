import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CpuComponent } from './components/hard/cpu/cpu.component';
import { MemoryComponent } from './components/hard/memory/memory.component';
import { HomeComponent } from './components/home/home.component';


const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'memory', component: MemoryComponent },
  { path: 'cpu', component: CpuComponent },
  { path: '**', component: HomeComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

 }

