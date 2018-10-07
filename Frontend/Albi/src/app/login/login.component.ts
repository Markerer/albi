import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {

  user: User = new User();

  correctUser: boolean;

  loginForm: FormGroup;
  createForm: FormGroup;

  focus: boolean;
  focus1: boolean;

  constructor(private userService: UserService, fb: FormBuilder) {
    this.loginForm = fb.group({
      "username": ["", Validators.required],
      "password": ["", Validators.required]
    });
    this.createForm = fb.group({
      "username": ["", Validators.required],
      "password": ["", Validators.required],
      "email": ["", [Validators.required, Validators.email]],
      "phone_number": ["", Validators.required],
      "address": ["", Validators.required]
    });
    this.correctUser = false;
  }

  ngOnInit() {
	  var body = document.getElementsByTagName('body')[0];
        body.classList.add('login-page');
  }

  ngOnDestroy() {
    var body = document.getElementsByTagName('body')[0];
        body.classList.remove('login-page');
  }

  createUser(username: String, password: String, email: String, phone_number: String, address: String): void {
    
      this.user.username = username;
      this.user.password = password;
      this.user.address = address;
      this.user.email = email;
      this.user.phone_number = phone_number;
      this.userService.createUser(this.user).subscribe(
        user => { console.log(`User ${user.username} created`) }
      );
    
  }

  login(username: String, password: String): void {

    var response: String;

    this.userService.loginUser(username, password).subscribe(data => { console.log(data); response = data; });

    if (response === "OK") {
      this.correctUser = true;
      this.userService.getUser(username).subscribe(loggedUser => { this.user = loggedUser });
    }
    else {
      this.correctUser = false;
    }


  }
	

}
