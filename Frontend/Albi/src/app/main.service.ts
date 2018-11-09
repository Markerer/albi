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

  // Fő oldalon megjelenő hirdetések lekérése
  getMainScreen(page: number): Observable<Object[]> {
    return this.http.get<Object[]>(this.apiRoot + 'api/' + 'main/' + page);
  }

  // Egy lakás lekérése
  getFlatByID(flatID: String): Observable<Flat> {
    return this.http.get<Flat>(this.apiRoot + 'flat/' + flatID);
  }

  // Egy felhasználó minden lakásának lekérése
  getUserFlats(userID: String): Observable<Flat[]> {
    return this.http.get<Flat[]>(this.apiRoot + 'user/flats/' + userID);
  }

  // Új hirdetés feladása
  addAdvertisement(flat: Flat): Observable<Flat> {
    return this.http.post<Flat>(this.apiRoot + 'addflat/' + flat.userID,
      {
        "userID": `${flat.userID}`,
        "price": `${flat.price}`,
        "numberOfRooms": `${flat.numberOfRooms}`,
        "description": `${flat.description}`,
        "email": `${flat.email}`,
        "phone_number": `${flat.phone_number}`,
        "zipcode": `${flat.zipcode}`,
        "city": `${flat.city}`,
        "address": `${flat.address}`,
        "forSale": `${flat.forSale}`
      },
      httpOptions);
  }

  // Lakás adatainak módosítása
  updateFlat(flat: Flat): Observable<String> {
    return this.http.put(this.apiRoot + 'flats/',
      {
        "_id": `${flat._id}`,
        "userID": `${flat.userID}`,
        "price": `${flat.price}`,
        "numberOfRooms": `${flat.numberOfRooms}`,
        "description": `${flat.description}`,
        "email": `${flat.email}`,
        "phone_number": `${flat.phone_number}`,
        "zipcode": `${flat.zipcode}`,
        "city": `${flat.city}`,
        "address": `${flat.address}`,
        "forSale": `${flat.forSale}`
      },
      { responseType: 'text' });
  }
  
  deleteFlat(flatID: String): Observable<String> {
    return this.http.delete(this.apiRoot + 'flat/' + flatID, { responseType: 'text' });
  }

}
