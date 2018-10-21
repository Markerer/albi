import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { Flat } from '../flat';
import { DataService } from '../data.service';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  flats: Flat[];
  user: User;
  undefinedUser: boolean = false;

  constructor(private data: DataService, private mainService: MainService, private router: Router) { }

  ngOnInit() {
    this.data.currentData.subscribe(data => {
      this.user = data;
      console.log(this.user);
      if (this.user === undefined || this.user === null) {
        this.undefinedUser = true;
      }
    });
  }

  navigateToAdding() {
    this.router.navigate(['main/addadvertisement']);
  }

  getFlats(): void {
    this.mainService.getMainScreen().subscribe(data => { this.flats = data; console.log(this.flats); });
  }

  logout(): void {
    this.user = null;
    this.router.navigate(['']);
  }

  login(): void {
    this.router.navigate(['']);
  }




}
