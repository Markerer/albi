import { Component, OnInit, ElementRef } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {


  constructor(public location: Location, private element: ElementRef, private router: Router) {
  }

  navigateToMain(): void {
    this.router.navigate(['main']);
  }

  navigateToMyProfile(): void {
    this.router.navigate(['myprofile']);
  }

  navigateToMyAds(): void {
    this.router.navigate(['main/myadvertisements']);
  }

  navigateToAddAd(): void {
    this.router.navigate(['main/addadvertisement']);
  }

  navigateToLogOut(): void {
    this.router.navigate(['']);
  }


  ngOnInit() {}
}

