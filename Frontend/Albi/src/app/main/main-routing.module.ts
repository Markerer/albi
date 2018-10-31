import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './main.component';
import { FlatComponent } from './flat/flat.component';

const routes: Routes = [
  { path: 'main', component: MainComponent },
  { path: 'main/:id', component: FlatComponent }
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
