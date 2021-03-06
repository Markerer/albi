import { Component, OnInit, ElementRef } from '@angular/core';
import { Location } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {


  constructor(public location: Location, private element: ElementRef, private router: Router,
  private userService: UserService) {
    router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        if (event.url === '/blank') {
          this.router.navigate(['main']);
        }
      }
    });
  }

  navigateToMain(): void {
    this.router.navigate(['blank']);

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
    this.userService.logout();
    this.router.navigate(['']);
  }


  ngOnInit() {}
}

