import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../data.service';
import { MainService } from '../main.service';
import { User } from '../user';
import { Flat } from '../flat';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-flat',
  templateUrl: './flat.component.html',
  styleUrls: ['./flat.component.scss']
})
export class FlatComponent implements OnInit {

  undefinedUser: boolean = false;
  owner: boolean;

  flat: Flat;
  user: User;

  updateFlatForm: FormGroup;

  private _success = new Subject<string>();
  successMessage: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private data: DataService,
    private mainService: MainService,
    private fb: FormBuilder)
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

  getFlat(): void {
    //Az id kinyerése az URL címből.
    var id: String = this.activatedRoute.snapshot.paramMap.get('_id');
    console.log(id);
    this.mainService.getFlatByID(id).subscribe(data => {
      this.flat = data;
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
    /*
    this.mainService.updateFlat(this.flat).subscribe(
      flat => {
        console.log(flat);
      });
    */
    // 5 sec múlva main oldalra átírányítás
    setTimeout(() => {
      this.router.navigate(['main']);
    }, 5000);

  }

}
