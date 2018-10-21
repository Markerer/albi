import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { DataService } from '../data.service';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  user: User = new User();
  updateForm: FormGroup;
  undefinedUser: Boolean = false;

  private _success = new Subject<string>();
  successMessage: string;


  changeDatasInForm() {
    this.updateForm.controls['username'].setValue(this.user.username);
    this.updateForm.controls['username'].updateValueAndValidity();
    this.updateForm.controls['username'].markAsDirty();
    this.updateForm.controls['password'].setValue(this.user.password);
    this.updateForm.controls['password'].updateValueAndValidity();
    this.updateForm.controls['password'].markAsDirty();
    this.updateForm.controls['email'].setValue(this.user.email);
    this.updateForm.controls['email'].updateValueAndValidity();
    this.updateForm.controls['email'].markAsDirty();
    this.updateForm.controls['phone_number'].setValue(this.user.phone_number);
    this.updateForm.controls['phone_number'].updateValueAndValidity();
    this.updateForm.controls['phone_number'].markAsDirty();
    this.updateForm.controls['address'].setValue(this.user.address);
    this.updateForm.controls['address'].updateValueAndValidity();
    this.updateForm.controls['address'].markAsDirty();
  }


  constructor(private data: DataService, fb: FormBuilder, private router: Router, private userService: UserService) {
   
    if (!this.undefinedUser) {
      this.updateForm = fb.group({
        'username': [this.user.username, null],
        'password': [this.user.password, null],
        'email': [this.user.email, null],
        'phone_number': [this.user.phone_number, null],
        'address': [this.user.address, null]
      });
    }
  }

  ngOnInit() {
    this.data.currentData.subscribe(data => {
      if (data === undefined || data === null) {
        this.undefinedUser = true;
        this.router.navigate(['']);
      }
      else {
        this.user._id = data._id;
        this.user.username = data.username;
        this.user.password = data.password;
        this.user.address = data.address;
        this.user.email = data.email;
        this.user.phone_number = data.phone_number;
        this.changeDatasInForm();
        console.log(this.user);
      }
    });
    


    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);
  }

  public changeSuccessMessage(): void {
    this._success.next(`Your informations have been successfully updated, you will be redirected to the main page in 5 seconds!`);
  }


  updateUser(username: String, password: String, email: String, phone_number: String, address: String): void {
    if (!(username === "" || username === undefined)) {
       this.user.username = username;
    }
    if (!(password === "" || password === undefined)) {
      this.user.password = password;
    }
    if (!(address === "" || address === undefined)) {
      this.user.address = address;
    }
    if (!(email === "" || email === undefined)) {
      this.user.email = email;
    }
    if (!(phone_number === "" || phone_number === undefined)) {
      this.user.phone_number = phone_number;
    }

    this.userService.updateUser(this.user).subscribe(
      user => {
        console.log(`${user}`);
        this.data.changeData(this.user);
      });

    // 5 sec múlva main oldalra átírányítás
    setTimeout(() => {
      this.router.navigate(['main']);
    }, 5000);

  }

}
