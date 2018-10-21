import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { AddAdvertisementComponent } from './add-advertisement/add-advertisement.component';
import { IsSecureGuard } from './issecureguard.service';
import { ProfileComponent } from './profile/profile.component';


const routes: Routes = [
  { path: '', component: LoginComponent, canActivate: [IsSecureGuard] },
  { path: 'main', component: MainComponent, canActivate: [IsSecureGuard] },
  { path: 'main/addadvertisement', component: AddAdvertisementComponent, canActivate: [IsSecureGuard] },
  { path: 'myprofile', component: ProfileComponent, canActivate: [IsSecureGuard] },
  { path: '**', redirectTo: '' }
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)],
})

export class AppRoutingModule { }
