import { User } from './user';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';


@Injectable()
export class UserService {

  apiRoot: string = 'http://localhost:3000/';

  constructor(private http: HttpClient) { }
  
  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiRoot + 'user', user);
  }

  getUser(username: string): Observable<User> {
    return this.http.get<User>(this.apiRoot + 'user/' + username);
  }




}
