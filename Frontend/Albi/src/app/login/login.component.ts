import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {

  user: User = new User();

  loginForm: FormGroup;
  createForm: FormGroup;
  
  focus: boolean;
  focus1: boolean;

  constructor(private userService: UserService, fb: FormBuilder, private router: Router) {
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

  userLogger(response: String, usernameLogin: String): void {

    console.log(response);
    if (response === "OK") {
      this.userService.getUser(usernameLogin).subscribe(loggedUser => { this.user = loggedUser });
      console.log('oke volt');
      this.router.navigate(['/main']);      
    }
    else {
      console.log('nem volt oke');
    }
  }

  login(usernameLogin: String, passwordLogin: String): void {
  
    this.userService.loginUser(usernameLogin, passwordLogin)
      .subscribe(data => { this.userLogger(data, usernameLogin); console.log(data); });
  }
	

}
