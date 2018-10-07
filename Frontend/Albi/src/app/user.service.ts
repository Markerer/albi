import { User } from './user';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
    })
}


@Injectable()
export class UserService {

  apiRoot: string = 'http://localhost:3000/api/';

  constructor(private http: HttpClient) { }
  
  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiRoot + 'user',
      {
        "username": `${user.username}`,
        "password": `${user.password}`,
        "email": `${user.email}`,
        "phone_number": `${user.phone_number}`,
        "address": `${user.address}`
      },
      httpOptions);
  }

  getUser(username: String): Observable<User> {
    return this.http.get<User>(this.apiRoot + 'user/' + username);
  }

  loginUser(username: String, pw: String): Observable<Object> {
    return this.http.post(this.apiRoot + 'login',
      {
        "username": `${username}`,
        "password": `${pw}`
      },
      { responseType: 'text'});
  }


}
