import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {

  user: User = new User();
  token: string;

  private _success = new Subject<string>();
  successMessage: string;

  private _alert = new Subject<string>();
  alertMessage: string;

  private _alertTaken = new Subject<string>();
  alertTakenMessage: string;

  loginForm: FormGroup;
  createForm: FormGroup;
  
  focus: boolean;
  focus1: boolean;
  correctDatas: boolean = true;
  isTaken = false;

  constructor(private userService: UserService, fb: FormBuilder, private router: Router) {
    // Amennyiben megvan még a token és érvényes a főoldalra irányítjuk
    if (this.userService.isLoggedIn()) {
      this.router.navigate(['main']);
    }
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

    //A már létező username esetén kiírandó üzenet
    this._alertTaken.subscribe((message) => this.alertTakenMessage = message);
    this._alertTaken.pipe(
      debounceTime(5000)
    ).subscribe(() => this.alertTakenMessage = null);

    var body = document.getElementsByTagName('body')[0];
    body.classList.add('login-page');
  }

  ngOnDestroy() {
    var body = document.getElementsByTagName('body')[0];
    body.classList.remove('login-page');
  }
  //Az üzenetek
  public changeSuccessMessage(): void {
    this._success.next(`New user successfully created, you will be redirected to the main page in a few seconds!`);
  }

  public changeAlertMessage(): void {
    if (!this.correctDatas) {
      this._alert.next(`The given username and password are incorrect. \n Please check them.`);
    }
  }

  public changeAlertTakenMessage(): void {
    if (this.isTaken) {
      this._alertTaken.next(`The given username is already taken. \n Please choose an another username.`);
    }
  }


  createUser(username: String, password: String, email: String, phone_number: String, address: String): void {
    
      this.user.username = username;
      this.user.password = password;
      this.user.address = address;
      this.user.email = email;
      this.user.phone_number = phone_number;
    this.userService.createUser(this.user).subscribe(
      response => {
        if (response === 'The username is already taken.') {
          this.isTaken = true;
          this.changeAlertTakenMessage();
        }
        else {
          // 5 sec múlva belépés ezzel az accounttal
          this.changeSuccessMessage();
          this.isTaken = false;
        setTimeout(() => {
           this.login(username, password);
        }, 5000);
        }
      }
      );
  }

  userLogger(response: String, usernameLogin: String): void {

    console.log(response);
    if (response === "OK") {
      this.userService.getUser(usernameLogin).subscribe(loggedUser => {
        this.user = loggedUser;
        console.log(this.user);
        this.userService.setSession(this.token);
        this.userService.setUser(this.user);
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
      .subscribe(data => {
        var msg: String;
        msg = data['message'];
        if (msg === ('OK')) {
          this.token = data['token'];
          console.log(this.token);
        } else {
          console.log('nem egyezett');
        }
        this.userLogger(msg, usernameLogin);
        console.log(data);
        console.log(msg);
      }, (err) => {
          this.correctDatas = false;
          this.changeAlertMessage();
          console.log("loginunath");
      });
  }
	

}
