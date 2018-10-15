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
import { MainModule } from './main/main.module';
import { IsSecureGuard } from './issecureguard.service';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MainComponent,
    AddAdvertisementComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MainModule,
  ],
  providers: [UserService, MainService, DataService, IsSecureGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
