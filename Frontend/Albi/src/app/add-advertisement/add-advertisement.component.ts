import { Component, OnInit, Input, Output } from '@angular/core';
import { User } from '../user';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MainService } from '../main.service';
import { Flat } from '../flat';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';
import { Router } from '@angular/router';
import { ImageService } from '../image.service';
import { Image } from '../image';
import { UserService } from '../user.service';
import { EventEmitter } from 'events';
import { AppSettings } from '../appsettings';

@Component({
  selector: 'app-add-advertisement',
  templateUrl: './add-advertisement.component.html',
  styleUrls: ['./add-advertisement.component.scss']
})
export class AddAdvertisementComponent implements OnInit {

  user: User = new User();
  undefinedUser: boolean;
  advertisementCreated: boolean = false;
  createdFlat: Flat = new Flat();

  selectedFile: File;

  private _success = new Subject<string>();
  successMessage: string;

  createFlatForm: FormGroup;

  constructor(fb: FormBuilder, private router: Router, private mainService: MainService,
    private imageService: ImageService, private userService: UserService) {
    this.createFlatForm = fb.group({
      "price": ["", Validators.required],
      "numberOfRooms": ["", Validators.required],
      "description": ["", Validators.required],
      'email': ["", Validators.required],
      'phone_number': ["", Validators.required],
      'zipCode': ["", Validators.required],
      'city': ["", Validators.required],
      "address": ["", Validators.required],
      "type": ["", Validators.required]
    });
  }

  ngOnInit() {

    if (this.userService.isLoggedIn()) {
      var temp = JSON.parse(localStorage.getItem("user"));
      this.user = temp;
     // console.log(this.user);
      this.undefinedUser = false;
    }
    else {
      this.undefinedUser = true;
      this.router.navigate(['']);
      if (this.userService.isLoggedOut()) {
        this.userService.logout();
      }
    }
    
    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);
  }

  public changeSuccessUploadMsg(): void {
    this._success.next(`Your picture is being uploaded. Wait a few seconds, please.`);
  }

  createFlat(price: Number, numberOfRooms: Number, description: String, email: String, phone_number: String, zipCode: Number, city: String, address: String, type: String): void {
    this.createdFlat.userID = this.user._id;
    this.createdFlat.price = price.toString();
    this.createdFlat.numberOfRooms = numberOfRooms.toString();
    this.createdFlat.description = description;
    this.createdFlat.email = email;
    this.createdFlat.phone_number = phone_number;
    this.createdFlat.zipCode = zipCode.toString();
    this.createdFlat.city = city;
    this.createdFlat.address = address;
   // console.log(type);
    if (type === "underlease") {
      this.createdFlat.forSale = false;
    } else if (type === "sale") {
      this.createdFlat.forSale = true;
    }
   // console.log(this.createdFlat);
    //A belépett user továbbadása
    this.mainService.addAdvertisement(this.createdFlat).subscribe(addedFlat => {
   //   console.log(addedFlat);
      this.createdFlat = addedFlat;
      this.advertisementCreated = true;
    });

  }

  navigateToMain() {
    this.router.navigate(['main']);
  }

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  // Kép feltöltése
  uploadImage() {
    if (!(this.selectedFile === null || this.selectedFile === undefined)) {
      const uploadData = new FormData();
      uploadData.append('image', this.selectedFile, this.selectedFile.name);
      this.imageService.uploadImage(uploadData, this.createdFlat._id).subscribe(object => {
     //   console.log(object);
        this.changeSuccessUploadMsg();
        this.getImages();
      });
    }
  }

  // Adott kép törlése
  deleteImage(imageID: String): void {
    this.imageService.deleteImage(imageID).subscribe(response => {
     // console.log(response);
      this.getImages();
    });
  }

  // Képek lekérése
  getImages() {
    this.createdFlat.images = [];
    this.createdFlat.firstImage = new Image();
    this.getImageUrls(this.createdFlat._id, this.createdFlat);
  }

  // A lakás képeinek (ID és fájlnév) beállítása
  getImageUrls(flatID: String, flat: Flat) {
    this.imageService.getFlatImageIDs(flatID).subscribe(data => {
      flat.images = [];
      for (let i = 0; i < data.length; i++) {
        var temp = new Image();
        temp = data[i];
        temp.filename = AppSettings.API_ROOT + data[i].filename;
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
