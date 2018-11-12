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

  private _alertTaken = new Subject<string>();
  alertTakenMessage: string;

  loginForm: FormGroup;
  createForm: FormGroup;
  focus: boolean;
  focus1: boolean;
  correctDatas: boolean;
  isTaken = false;

  constructor(private userService: UserService, fb: FormBuilder, private router: Router, private data: DataService) {
    this.loginForm = fb.group({
      'usernameLogin': ['', Validators.required],
      'passwordLogin': ['', Validators.required]
    });
    this.createForm = fb.group({
      'username': ['', Validators.required],
      'password': ['', Validators.required],
      'email': ['', [Validators.required, Validators.email]],
      'phone_number': ['', Validators.required],
      'address': ['', Validators.required]
    });
    this.correctDatas = true;
  }

  ngOnInit() {

    // A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);

    // A sikertelen bejelentkezés üzenet
    this._alert.subscribe((message) => this.alertMessage = message);
    this._alert.pipe(
      debounceTime(5000)
    ).subscribe(() => this.alertMessage = null);

    // A már létező username esetén kiírandó üzenet
    this._alertTaken.subscribe((message) => this.alertTakenMessage = message);
    this._alertTaken.pipe(
      debounceTime(5000)
    ).subscribe(() => this.alertTakenMessage = null);

    const body = document.getElementsByTagName('body')[0];
    body.classList.add('login-page');
  }

  // Az üzenetek
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
        } else {
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
    if (response === 'OK') {
      this.userService.getUser(usernameLogin).subscribe(loggedUser => {
        this.user = loggedUser;
        console.log(this.user);

        this.data.changeData(this.user);
        this.router.navigate(['main']);
      });
    } else {
      this.correctDatas = false;
      this.changeAlertMessage();
    }
  }

  login(usernameLogin: String, passwordLogin: String): void {
    this.userService.loginUser(usernameLogin, passwordLogin)
      .subscribe(data => { this.userLogger(data, usernameLogin); console.log(data); });
  }
}
