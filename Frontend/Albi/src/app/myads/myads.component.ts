import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { Router } from '@angular/router';
import { Flat } from '../flat';
import { User } from '../user';
import { DataService } from '../data.service';
import { isNull, isUndefined } from 'util';

@Component({
  selector: 'app-myads',
  templateUrl: './myads.component.html',
  styleUrls: ['./myads.component.scss']
})
export class MyadsComponent implements OnInit {

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
        this.router.navigate(['']);
      } else {
        this.flats = [];
        this.getFlats(this.user._id);
      }
    });
  }


  ngOnDestroy() {
    this.removeItemsFromFlats();
  }

  removeItemsFromFlats(): void {
    if (!isUndefined(this.flats)) {
      this.flats.splice(0);
    }
  }

  getFlats(userID: String): void {
    this.mainService.getUserFlats(userID).subscribe(data => {

      for (let i = 0; i < data.length; i++) {
        var temp: Flat = new Flat();
        temp.address = data[i].address;
        temp.description = data[i].description;
        temp.email = data[i].email;
        temp.hasAttachment = data[i].hasAttachment;
        temp.numberOfRooms = data[i].numberOfRooms;
        temp.phone_number = data[i].phone_number;
        temp.price = data[i].price;
        temp.userID = data[i].userID;
        temp._id = data[i]._id;
        this.flats.push(temp);

      }
      console.log(this.flats);
    });
  }



}
