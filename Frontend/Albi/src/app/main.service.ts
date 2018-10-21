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

  apiRoot: string = 'http://localhost:3000/';

  constructor(private http: HttpClient) { }


  getMainScreen(): Observable<Flat[]> {
    return this.http.get<Flat[]>(this.apiRoot + 'main').map(data => data['0']);
  }

  getFlatByID(flatID: String): Observable<Flat> {
    return this.http.get<Flat>(this.apiRoot + 'flat' + flatID).map(data => data['0']);
  }

  getUserFlats(userID: String): Observable<Flat[]> {
    return this.http.get<Flat[]>(this.apiRoot + 'user/flats/' + userID).map(data => data['0']);
  }

  addAdvertisement(flat: Flat): Observable<Flat> {
    return this.http.post<Flat>(this.apiRoot + 'addflat/' + flat.userID,
      {
        "userID": `${flat.userID}`,
        "price": `${flat.price}`,
        "numberOfRooms": `${flat.numberOfRooms}`,
        "description": `${flat.description}`,
        "email": `${flat.email}`,
        "phone_number": `${flat.phone_number}`,
        "address": `${flat.address}`,
        "hasAttachment": `${flat.hasAttachment}`
      },
      httpOptions);
  }
}
