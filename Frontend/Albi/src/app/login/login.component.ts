import { Component, OnInit } from '@angular/core';
import { Http, Response, RequestOptions, Headers, HttpModule } from '@angular/http';
import { UserService } from '../user.service';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {

  user: User;

  focus;
  focus1;
  apiRoot: string = "http://httpbin.org";

  constructor(private userService:UserService) { }

  ngOnInit() {
	  var body = document.getElementsByTagName('body')[0];
        body.classList.add('login-page');
  }

  ngOnDestroy() {
    var body = document.getElementsByTagName('body')[0];
        body.classList.remove('login-page');
  }

  createUser(username: HTMLInputElement, pw: HTMLInputElement): void {
    this.user.username = username.value;
    this.user.password = pw.value;
    this.userService.createUser(this.user).subscribe(
      user => { console.log(`User ${user.username} created`) }
    );
  }

  login(username: HTMLInputElement, pw: HTMLInputElement): void {
    console.log(`${username.value} logged in`);
   /* let url = `${this.apiRoot}/get`;
    this.http.get(url).subscribe(res => console.log(res.json()));*/
  }
	

}
