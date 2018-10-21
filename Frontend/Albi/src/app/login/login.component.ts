import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {

  user: User = new User();

  private _success = new Subject<string>();
  successMessage: string;

  private _alert = new Subject<string>();
  alertMessage: string;

  loginForm: FormGroup;
  createForm: FormGroup;
  
  focus: boolean;
  focus1: boolean;
  correctDatas: boolean = true;

  constructor(private userService: UserService, fb: FormBuilder, private router: Router, private data: DataService) {
    this.loginForm = fb.group({
      "usernameLogin": ["", Validators.required],
      "passwordLogin": ["", Validators.required]
    });
    this.createForm = fb.group({
      "username": ["", Validators.required],
      "password": ["", Validators.required],
      "email": ["", [Validators.required, Validators.email]],
      "phone_number": ["", Validators.required],
      "address": ["", Validators.required]
    });
  }

  ngOnInit() {

    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);

    //A sikertelen bejelentkezés üzenet
    this._alert.subscribe((message) => this.alertMessage = message);
    this._alert.pipe(
      debounceTime(5000)
    ).subscribe(() => this.alertMessage = null);

    var body = document.getElementsByTagName('body')[0];
    body.classList.add('login-page');
  }

  ngOnDestroy() {
    var body = document.getElementsByTagName('body')[0];
    body.classList.remove('login-page');
  }
  //Az üzenetek
  public changeSuccessMessage(): void {
    this._success.next(`New user successfully created, you will be redirected to the main page in 5 seconds!`);
  }

  public changeAlertMessage(): void {
    if (!this.correctDatas) {
      this._alert.next(`The given username and password are incorrect. \n Please check them.`);
    }
  }


  createUser(username: String, password: String, email: String, phone_number: String, address: String): void {
    
      this.user.username = username;
      this.user.password = password;
      this.user.address = address;
      this.user.email = email;
      this.user.phone_number = phone_number;
      this.userService.createUser(this.user).subscribe(
        user => {
          console.log(`User ${user.username} created`);
      });

    // 5 sec múlva belépés ezzel az accounttal
      setTimeout(() => {
         this.login(username, password);
      }, 5000);

  }

  userLogger(response: String, usernameLogin: String): void {

    console.log(response);
    if (response === "OK") {
      this.userService.getUser(usernameLogin).subscribe(loggedUser => {
        this.user._id = loggedUser._id;
        this.user.username = loggedUser.username;
        this.user.password = loggedUser.password;
        this.user.address = loggedUser.address;
        this.user.email = loggedUser.email;
        this.user.phone_number = loggedUser.phone_number;
        console.log(loggedUser);
        console.log(this.user);

        this.data.changeData(this.user);
        this.router.navigate(['main']);
      });
    }
    else {
      this.correctDatas = false;
      this.changeAlertMessage();
    }
  }

  login(usernameLogin: String, passwordLogin: String): void {
  
    this.userService.loginUser(usernameLogin, passwordLogin)
      .subscribe(data => { this.userLogger(data, usernameLogin); console.log(data); });
  }
	

}
