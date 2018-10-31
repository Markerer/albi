import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { Flat } from '../flat';
import { DataService } from '../data.service';
import { User } from '../user';
import { Router } from '@angular/router';
import { isNull, isUndefined } from 'util';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  flats: Flat[];
  collectionSize: number;
  page: number = 1;
  previousPage: number;
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
        this.page = 1;
        this.getFlatsByPage(this.page);
      }
    });
  }

  loadPage(page: number): void {
    if (page !== this.page) {
      this.page = page;
      this.removeItemsFromFlats();
      this.getFlatsByPage(this.page);
    }
  }

  removeItemsFromFlats(): void {
    if (!isUndefined(this.flats)) {
    this.flats.splice(0);
  }
  }
   

  navigateToAdding() {
    this.router.navigate(['main/addadvertisement']);
  }

  // A lekérdezés visszaad egy "docs" tömböt, amiben vannak a lakások
  // Amellett még visszaad "total" számot (összes rekord száma), illetve az aktuális oldal számát,
  // illetve még az összes oldal számát is
  getFlatsByPage(page: number): void {
    this.mainService.getMainScreen(page).subscribe(data => {
      var i: number = 0;
      var objects: Flat[];
      var num: number;
      objects = data["docs"];
      num = data["total"];
      this.collectionSize = num;
      for (i = 0; i < objects.length; i++) {
        var temp: Flat = new Flat();
        temp.address = objects[i].address;
        temp.description = objects[i].description;
        temp.email = objects[i].email;
        temp.hasAttachment = objects[i].hasAttachment;
        temp.numberOfRooms = objects[i].numberOfRooms;
        temp.phone_number = objects[i].phone_number;
        temp.price = objects[i].price;
        temp.userID = objects[i].userID;
        temp._id = objects[i]._id;
        this.flats.push(temp);

      }
      console.log(this.flats);
    });
  }

  login(): void {
    this.router.navigate(['']);
  }




}
