import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { Router } from '@angular/router';
import { Flat } from '../flat';
import { User } from '../user';
import { isNull, isUndefined } from 'util';
import { ImageService } from '../image.service';
import { Image } from '../image';
import { UserService } from '../user.service';

@Component({
  selector: 'app-myads',
  templateUrl: './myads.component.html',
  styleUrls: ['./myads.component.scss']
})
export class MyadsComponent implements OnInit {

  flats: Flat[];
  user: User;
  undefinedUser: boolean;


  constructor(private mainService: MainService, private userService: UserService,
    private router: Router, private imageService: ImageService) { }
  

  ngOnInit() {

    if (localStorage.getItem("user") && this.userService.isLoggedIn()) {
      var temp = JSON.parse(localStorage.getItem("user"));
      this.user = temp;
      console.log(this.user);
      this.undefinedUser = false;
      this.flats = [];
      this.getFlats(this.user._id);
    } else {
      this.undefinedUser = true;
      this.router.navigate(['']);
      if (this.userService.isLoggedOut()) {
        this.userService.logout();
      }
    }
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
        temp = data[i];
        temp.firstImage = new Image();
        this.getImageUrls(temp._id, temp);
        this.flats.push(temp);

      }
      console.log(this.flats);
    });
  }


  // A lakás képeinek (ID és fájlnév) beállítása
  getImageUrls(flatID: String, flat: Flat) {
    this.imageService.getFlatImageIDs(flatID).subscribe(data => {
      flat.images = [];
      for (let i = 0; i < data.length; i++) {
        var temp = new Image();
        temp = data[i];
        temp.filename = "http://localhost:3000/" + data[i].filename;
        flat.images.push(temp);
      }
      flat.firstImage = new Image();
      if (flat.images[0] === undefined) {
        flat.noImageFound = true;
        flat.firstImage.filename = "assets/img/download.png";
      }
      else {
        flat.noImageFound = false;
        flat.firstImage.filename = flat.images[0].filename;
      }
    });
  }


}
