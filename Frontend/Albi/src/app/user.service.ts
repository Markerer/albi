import { User } from './user';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import 'rxjs/add/operator/map';
import * as moment from "moment";
import 'rxjs/add/operator/catch';
import { AppSettings } from './appsettings';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
    })
}

@Injectable()
export class UserService {

  constructor(private http: HttpClient) { }
  
  createUser(user: User): Observable<string> {
    return this.http.post(AppSettings.API_ROOT + 'api/' + 'user',
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
    return this.http.get<User>(AppSettings.API_ROOT + 'api/' + 'users/' + username);
  }

  updateUser(user: User): Observable<Object> {
    return this.http.put(AppSettings.API_ROOT + 'api/' + 'user/',
      {
        "_id": `${user._id}`,
        "username": `${user.username}`,
        "password": `${user.password}`,
        "email": `${user.email}`,
        "phone_number": `${user.phone_number}`,
        "address": `${user.address}`
      }, {
        responseType: 'text',
        headers: {
          'Authorization': 'Bearer' + ' ' + localStorage.getItem("token"),
          'Content-Type': 'application/json'
        }
      });
  }

  loginUser(username: String, pw: String): Observable<Object> {
    return this.http.post(AppSettings.API_ROOT + 'api/' + 'login',
      {
        "username": `${username}`,
        "password": `${pw}`
      },
      httpOptions).catch(e => {
        //console.log("before 401");
        if (e.status === 401) {
         // console.log("unathservice");
          return Observable.throw('Unauthorized');
        }
      });
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
