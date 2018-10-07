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
    return this.http.post<User>(this.apiRoot + 'user', user, httpOptions);
  }

  getUser(username: string): Observable<User> {
    return this.http.get<User>(this.apiRoot + 'user/' + username);
  }

  loginUser(username: string, pw: string): Observable<string> {
    var array: string[];
    array = [username, pw];

    return this.http.post<string>(this.apiRoot + 'login', array, httpOptions);
  }


}
