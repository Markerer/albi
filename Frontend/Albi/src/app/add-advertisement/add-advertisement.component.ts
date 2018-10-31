import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { DataService } from '../data.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MainService } from '../main.service';
import { Flat } from '../flat';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-advertisement',
  templateUrl: './add-advertisement.component.html',
  styleUrls: ['./add-advertisement.component.scss']
})
export class AddAdvertisementComponent implements OnInit {

  user: User = new User();
  undefinedUser: boolean = false;
  createdFlat: Flat = new Flat();

  private _success = new Subject<string>();
  successMessage: string;

  createFlatForm: FormGroup;

  constructor(private data: DataService, fb: FormBuilder, private router: Router, private mainService: MainService) {
    this.createFlatForm = fb.group({
      "price": ["", Validators.required],
      "numberOfRooms": ["", Validators.required],
      "description": ["", Validators.required],
      "address": ["", Validators.required],
      "hasAttachment": [""]
    });
  }

  ngOnInit() {
    this.data.currentData.subscribe(data => {
      if (data === undefined || data === null) {
        this.undefinedUser = true;
        this.router.navigate(['']);
      } else {
        this.user._id = data._id;
        this.user.username = data.username;
        this.user.password = data.password;
        this.user.address = data.address;
        this.user.email = data.email;
        this.user.phone_number = data.phone_number;
        console.log(this.user);
      }
    });
    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(4000)
    ).subscribe(() => this.successMessage = null);
  }

  public changeSuccessMessage(): void {
    this._success.next(`New advertisement successfully created! \n You will be redirected to the main page in a few seconds.`);
  }

  createFlat(price: String, numberOfRooms: String, description: String, address: String, hasAttachment: Boolean): void {
    this.createdFlat.userID = this.user._id;
    this.createdFlat.price = price;
    this.createdFlat.numberOfRooms = numberOfRooms;
    this.createdFlat.description = description;
    this.createdFlat.email = this.user.email;
    this.createdFlat.phone_number = this.user.phone_number;
    this.createdFlat.address = address;
    this.createdFlat.hasAttachment = hasAttachment;
    //A belépett user továbbadása
    this.mainService.addAdvertisement(this.createdFlat).subscribe(addedFlat => { console.log(addedFlat); this.data.changeData(this.user);});
    setTimeout(() => {
      this.router.navigate(['main']);
    }, 4000);
  }

}
