import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Flat } from './flat';


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable()
export class MainService {

  apiRoot: string = 'http://localhost:3000/api/';

  constructor(private http: HttpClient) { }


  getMainScreen(): Observable<Flat[]> {
    return this.http.get<Flat[]>(this.apiRoot + 'main');
  }


}
