import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { Flat } from '../flat';
import { DataService } from '../data.service';
import { User } from '../user';
import { Router } from '@angular/router';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  flats: Flat[];
  collectionSize: number;
  page: number;
  user: User;
  undefinedUser: boolean = false;

  constructor(private data: DataService, private mainService: MainService, private router: Router) { }

  ngOnInit() {
    this.data.currentData.subscribe(data => {
      this.user = data;
      console.log(this.user);
      if (this.user === undefined || this.user === null) {
        this.undefinedUser = true;
        this.router.navigate(['']);
      } else {
        this.flats = [];
        for (let i = 0; i < 10; i++) this.flats.push(new Flat());
        this.page = 1;
        this.getFlatsByPage(this.page);
      }
    });
  }

  navigateToAdding() {
    this.router.navigate(['main/addadvertisement']);
  }

  getFlatsByPage(page: number): void {
    this.mainService.getMainScreen(page).subscribe(data => {
      var i: number = 0;
      var objects: Flat[];
      var num: number;
      objects = data["docs"];
      num = data["total"];
      this.collectionSize = num;
      for (i = 0; i < objects.length; i++) {
        this.flats[i].address = objects[i].address;
        this.flats[i].description = objects[i].description;
        this.flats[i].email = objects[i].email;
        this.flats[i].hasAttachment = objects[i].hasAttachment;
        this.flats[i].numberOfRooms = objects[i].numberOfRooms;
        this.flats[i].phone_number = objects[i].phone_number;
        this.flats[i].price = objects[i].price;
        this.flats[i].userID = objects[i].userID;
        this.flats[i]._id = objects[i]._id;

      }
      console.log(this.flats);
    });
  }

  login(): void {
    this.router.navigate(['']);
  }




}
