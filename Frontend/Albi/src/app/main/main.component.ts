import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { Flat } from '../flat';
import { DataService } from '../data.service';
import { User } from '../user';
import { Router } from '@angular/router';
import { isNull, isUndefined } from 'util';
import { ImageService } from '../image.service';
import { Image } from "../image";
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  flats: Flat[];
  search: Flat;
  collectionSize: number;
  page: number = 1;
  previousPage: number;
  user: User;
  undefinedUser: boolean = false;

  searchFlatForm: FormGroup;

  constructor(private fb: FormBuilder, private data: DataService, private mainService: MainService, private router: Router, private imageService: ImageService)
  {
    this.searchFlatForm = fb.group({
      "maxprice": ["", null],
      "numberOfRooms": ["", null],
      'zipCode': ["", null],
      'city': ["", null],
      "type": ["", null]
    });
  }

  ngOnInit() {
    this.data.currentData.subscribe(data => {
      if (data === undefined || data === null) {
        this.undefinedUser = true;
        this.router.navigate(['']);
      } else {
        this.user = data;
        this.flats = [];
        this.page = 1;
        this.getFlatsByPage(this.page);
      }
    });
  }

  loadPage(page: number): void {
    if (page !== this.page) {
      this.page = page;
      this.removeItems();
      if (this.search === undefined || this.search === null) {
        this.getFlatsByPage(this.page);
      } else {
        this.getFlatsBySearch(this.page);
      }
    }
  }

  removeItems(): void {
    if (!isUndefined(this.flats)) {
      this.flats.splice(0);
  }
  }
/*
  // Kép előállítása Blob objektumból
  createImageFromBlob(image: Blob) {
    let reader = new FileReader();
    reader.addEventListener("load", () => {
      this.imageToShow = reader.result;
    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }

  getImageFromService(filename: String): void {

    this.imageService.getFlatImage(filename).subscribe(data => {
      this.isImageLoading = true;
      this.createImageFromBlob(data);
      this.isImageLoading = false;
    }, error => {
      this.isImageLoading = false;
      console.log(error);
    });
  }
*/
  // A lakás képeinek (ID és fájlnév) beállítása
  getImageUrls(flatID: String, flat: Flat) {
    this.imageService.getFlatImageIDs(flatID).subscribe(data => {
      flat.images = [];
      for (let i = 0; i < data.length; i++) {
        var temp = new Image();
        temp = data[i];
        temp.filename = "http://localhost:3000/" + data[i].filename;
        //console.log(temp);
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
        temp = objects[i];
        temp.firstImage = new Image();
        this.getImageUrls(temp._id, temp);
        this.flats.push(temp);
      }
      console.log(this.flats);
    });
  }

  
  getFlatsBySearch(page: number): void {
    this.removeItems();
    this.mainService.searchFlat(this.search, page).subscribe(data => {
      console.log(data);
      var objects: Flat[];
      var num: number;
      objects = data["docs"];
      num = data["total"];
      this.collectionSize = num;
      for (let i of objects) {
        var temp: Flat = new Flat();
        temp = i;
        temp.firstImage = new Image();
        this.getImageUrls(i._id, i);
        this.flats.push(i);
      }
      console.log(this.flats);
    });
  }


  searchFlat(maxprice: Number, numberOfRooms: Number, zipCode: Number, city: String, type: String): void {

    this.search = new Flat();

    if (maxprice === undefined || maxprice === null) {
      this.search.price = "";
    } else {
      this.search.price = maxprice.toString();
    }
    if (numberOfRooms === undefined || numberOfRooms === null) {
      this.search.numberOfRooms = "";
    } else {
      this.search.numberOfRooms = numberOfRooms.toString();
    }
    if (zipCode === undefined || zipCode === null) {
      this.search.zipCode = "";
    } else {
      this.search.zipCode = zipCode.toString();
    }
    if (city === undefined || city === "") {
      this.search.city = "";
    } else {
      this.search.city = city;
    }
    if (type === "underlease") {
      console.log("albérlet");
      this.search.forSale = false;
    } else if (type === "sale") {
      console.log("eladó");
      this.search.forSale = true;
    }
    console.log(this.search);
    this.page = 1;
    this.getFlatsBySearch(this.page);
  }

}
