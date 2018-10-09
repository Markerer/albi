import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { DataService } from '../data.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MainService } from '../main.service';
import { Flat } from '../flat';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';

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

  constructor(private data: DataService, fb: FormBuilder, private mainService: MainService) {
    this.createFlatForm = fb.group({
      "flatname": ["", Validators.required],
      "address": ["", Validators.required],
      "hasAttachment": ["", [Validators.required]]
    });
  }

  ngOnInit() {
    this.data.currentData.subscribe(data => {
      if (data === undefined || data === null) {
        this.undefinedUser = true;
      } else {
        this.user.username = data.username;
        this.user.password = data.password;
        this.user.address = data.address;
        this.user.email = data.email;
        this.user.phone_number = data.phone_number;
        console.log(this.user);
      }
    });
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);
  }

  public changeSuccessMessage(): void {
    this._success.next(`New advertisement successfully created!`);
  }

  createFlat(flatname: String, address: String, hasAttachment: boolean): void {
    this.createdFlat.username = this.user.username;
    this.createdFlat.flatname = flatname;
    this.createdFlat.email = this.user.email;
    this.createdFlat.address = address;
    this.createdFlat.hasAttachment = hasAttachment;
    this.mainService.addAdvertisement(this.createdFlat).subscribe(addedFlat => { console.log(addedFlat); });
  }

}
