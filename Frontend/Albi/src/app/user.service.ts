import { User } from './user';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import 'rxjs/add/operator/map';
import * as moment from "moment";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
    })
}

var token: string = "Bearer ";

const httpAuth = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': token
  })
}

@Injectable()
export class UserService {

  apiRoot: string = 'http://localhost:3000/api/';

  constructor(private http: HttpClient) { }
  
  createUser(user: User): Observable<String> {
    return this.http.post(this.apiRoot + 'user',
      {
        "username": `${user.username}`,
        "password": `${user.password}`,
        "email": `${user.email}`,
        "phone_number": `${user.phone_number}`,
        "address": `${user.address}`
      },
      { responseType: 'text' });
  }

  getUser(username: String): Observable<User> {
    return this.http.get<User>(this.apiRoot + 'users/' + username);
  }

  updateUser(user: User): Observable<Object> {
    token += localStorage.getItem("token");
    console.log(token);
    return this.http.put(this.apiRoot + 'user/',
      {
        "_id": `${user._id}`,
        "username": `${user.username}`,
        "password": `${user.password}`,
        "email": `${user.email}`,
        "phone_number": `${user.phone_number}`,
        "address": `${user.address}`
      }, httpAuth);
  }

  loginUser(username: String, pw: String): Observable<Object> {
    return this.http.post(this.apiRoot + 'login',
      {
        "username": `${username}`,
        "password": `${pw}`
      },
      httpOptions);
  }

  public setSession(token: string) {
    const expiresAt = moment().add(60, 'minutes');

    localStorage.setItem("token", token);
    localStorage.setItem("expires_at", JSON.stringify(expiresAt.valueOf()));

  }

  public setUser(user: User) {
    localStorage.setItem("user", JSON.stringify(user));
  }

  public logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("expires_at");
    localStorage.removeItem("user");
  }

  public isLoggedIn() {
    return moment().isBefore(this.getExpiration());
  }

  public isLoggedOut() {
    return !this.isLoggedIn();
  }

  private getExpiration() {
    const expiration = localStorage.getItem("expires_at");
    const expiresAt = JSON.parse(expiration);
    return moment(expiresAt);
  } 

}
