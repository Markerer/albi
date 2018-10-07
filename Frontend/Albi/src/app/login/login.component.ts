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

  user: User;

  correctUser: boolean;

  loginForm: FormGroup;
  createForm: FormGroup;

  focus: boolean;
  focus1: boolean;

  constructor(private userService: UserService, fb: FormBuilder) {
    this.loginForm = fb.group({
      'usernameLogin': ['', Validators.required],
      'pwLogin': ['', Validators.required]
    });
    this.createForm = fb.group({
      'usernameCreate': ['', Validators.required],
      'email': ['', [Validators.required, Validators.email]],
      'address': ['', Validators.required],
      'phone': ['', Validators.required],
      'pwCreate': ['', Validators.required]
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

  createUser(usernameCreate: HTMLInputElement, pwCreate: HTMLInputElement, email: HTMLInputElement, address: HTMLInputElement, phone: HTMLInputElement): void {
    
      this.user.username = usernameCreate.value;
      this.user.password = pwCreate.value;
      this.user.address = address.value;
      this.user.email = email.value;
      this.user.phone = phone.value;
      this.userService.createUser(this.user).subscribe(
        user => { console.log(`User ${user.username} created`) }
      );
    
  }

  login(usernameLogin: HTMLInputElement, pwLogin: HTMLInputElement): void {

    var response: string;

    this.userService.loginUser(usernameLogin.value, pwLogin.value).subscribe(data => { console.log(data); response = data; });

    if (response === "OK") {
      this.correctUser = true;
    }
    else {
      this.correctUser = false;
    }

  }
	

}
