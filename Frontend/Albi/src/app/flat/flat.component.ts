import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../data.service';
import { MainService } from '../main.service';
import { User } from '../user';
import { Flat } from '../flat';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ImageService } from '../image.service';
import { Image } from '../image';

@Component({
  selector: 'app-flat',
  templateUrl: './flat.component.html',
  styleUrls: ['./flat.component.scss']
})
export class FlatComponent implements OnInit {

  undefinedUser: boolean = false;
  visitorMode: boolean = false;
  owner: boolean;

  flat: Flat = new Flat();
  user: User;

  selectedFile: File;

  updateFlatForm: FormGroup;

  private _success = new Subject<string>();
  successMessage: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private data: DataService,
    private mainService: MainService,
    private fb: FormBuilder,
    private imageService: ImageService)
  {}

  ngOnInit() {
    this.data.currentData.subscribe(data => {
      if (data === undefined || data === null) {
        this.undefinedUser = true;
        this.router.navigate(['']);
      } else {
        this.user = data;
        this.getFlat();
      }
    });

    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);
  }

  public changeSuccessMessage(): void {
    this._success.next(`Your advertisement have been successfully updated, you will be redirected to the main page in a few seconds!`);
  }

  public changeSuccessUploadMsg(): void {
    this._success.next(`Your picture is being uploaded. Wait a few seconds, please.`);
  }

  getFlat(): void {
    //Az id kinyerése az URL címből.
    var id: String = this.activatedRoute.snapshot.paramMap.get('_id');
    this.mainService.getFlatByID(id).subscribe(data => {
      this.flat = data;
      this.flat.images = [];
      this.flat.firstImage = new Image();
      this.getImageUrls(this.flat._id, this.flat);
      console.log(this.flat);
      // Megnézzük, hogy saját hirdetésünk-e
      if (this.flat.userID === this.user._id) {
        this.owner = true;
      } else {
        this.owner = false;
      }
      this.updateFlatForm = this.fb.group({
        'price': [this.flat.price, null],
        'numberOfRooms': [this.flat.numberOfRooms, null],
        'description': [this.flat.description, null],
        'email': [this.flat.email, null],
        'phone_number': [this.flat.phone_number, null],
        'address': [this.flat.address, null],
        'hasAttachment': [this.flat.hasAttachment, null]
      });
    });
  }
  
  updateFlat(price: String, numberOfRooms: String, description: String, email: String, phone_number: String, address: String, hasAttachment: Boolean): void {
    if (!(price === "" || price === undefined)) {
      this.flat.price = price;
    }
    if (!(numberOfRooms === "" || numberOfRooms === undefined)) {
      this.flat.numberOfRooms = numberOfRooms;
    }
    if (!(description === "" || description === undefined)) {
      this.flat.description = description;
    }
    if (!(email === "" || email === undefined)) {
      this.flat.email = email;
    }
    if (!(phone_number === "" || phone_number === undefined)) {
      this.flat.phone_number = phone_number;
    }
    if (!(address === "" || address === undefined)) {
      this.flat.address = address;
    }
    this.flat.hasAttachment = hasAttachment;


    console.log(this.flat);


    this.mainService.updateFlat(this.flat).subscribe(
      msg => {
        console.log(msg);
      });
    
    // 5 sec múlva main oldalra átírányítás
    setTimeout(() => {
      this.router.navigate(['main']);
    }, 5000);

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

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  uploadImage() {
    const uploadData = new FormData();
    uploadData.append('image', this.selectedFile, this.selectedFile.name);
    this.imageService.uploadImage(uploadData, this.flat._id).subscribe(object =>
    {
      console.log(object);
      this.changeSuccessUploadMsg();
      this.getFlat();
    });
  }

  deleteImage(imageID: String): void {
    this.imageService.deleteImage(imageID).subscribe(response =>
    {
      console.log(response);
      this.getFlat();
    });
  }

  visitMode(): void {
    this.visitorMode = !this.visitorMode;
  }

}
