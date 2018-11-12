import { Component, OnInit, OnDestroy } from '@angular/core';
import { MainService } from '../main.service';
import { Router } from '@angular/router';
import { Flat } from '../flat';
import { User } from '../user';
import { DataService } from '../data.service';
import { isNull, isUndefined } from 'util';
import { ImageService } from '../image.service';
import { Image } from '../image';

@Component({
  selector: 'app-myads',
  templateUrl: './myads.component.html',
  styleUrls: ['./myads.component.scss']
})
export class MyadsComponent implements OnInit, OnDestroy {

  flats: Flat[];
  user: User;
  undefinedUser = false;


  constructor(private data: DataService, private mainService: MainService, private router: Router, private imageService: ImageService) { }


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
        let temp: Flat = new Flat();
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
        let temp = new Image();
        temp = data[i];
        temp.filename = 'http://localhost:3000/' + data[i].filename;
        flat.images.push(temp);
      }
      flat.firstImage = new Image();
      if (flat.images[0] === undefined) {
        flat.noImageFound = true;
        flat.firstImage.filename = 'assets/img/download.png';
      } else {
        flat.noImageFound = false;
        flat.firstImage.filename = flat.images[0].filename;
      }
    });
  }


}
