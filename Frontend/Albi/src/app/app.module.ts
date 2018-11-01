import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { UserService } from './user.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MainComponent } from './main/main.component';
import { AppRoutingModule } from './/app-routing.module';
import { MainService } from './main.service';
import { AddAdvertisementComponent } from './add-advertisement/add-advertisement.component';
import { DataService } from './data.service';
import { IsSecureGuard } from './issecureguard.service';
import { NavbarComponent } from './navbar/navbar.component';
import { ProfileComponent } from './profile/profile.component';
import { MyadsComponent } from './myads/myads.component';
import { NgbModule, NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { FlatComponent } from './flat/flat.component';
import { ImageService } from './image.service';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MainComponent,
    AddAdvertisementComponent,
    NavbarComponent,
    ProfileComponent,
    MyadsComponent,
    FlatComponent
  ],
  imports: [
    NgbModule.forRoot(),
    NgbCollapseModule,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [UserService, MainService, DataService, ImageService, IsSecureGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
