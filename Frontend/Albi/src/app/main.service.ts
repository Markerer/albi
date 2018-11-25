import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Flat } from './flat';
import { ChartData } from './chartdata';


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
  
  // Egy hirdetéshez tartozó megtekintési adatok lekérése
  getAdvertisementStats(flatID: String): Observable<ChartData[]> {
    return this.http.get<ChartData[]>(this.apiRoot + 'flat/' + 'dates/' + flatID);
  }

  // Egy hirdetés megtekintési számának növelése
  addViewingToAdvertisement(flatID: String, date: String): Observable<ChartData>{
    return this.http.post<ChartData>(this.apiRoot + 'date/' + flatID,
    {
      "date": `${date}`
    },
      {
        headers: {
          'Authorization': 'Bearer' + ' ' + localStorage.getItem("token"),
          'Content-Type': 'application/json'
        }
      });
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
        "zipCode": `${flat.zipCode}`,
        "city": `${flat.city}`,
        "address": `${flat.address}`,
        "forSale": `${flat.forSale}`
      },
      {
        headers: {
          'Authorization': 'Bearer' + ' ' + localStorage.getItem("token"),
          'Content-Type': 'application/json'
        }
      });
  }

  // Lakás adatainak módosítása
  updateFlat(flat: Flat): Observable<Object> {
    return this.http.put(this.apiRoot + 'flats/',
      {
        "_id": `${flat._id}`,
        "userID": `${flat.userID}`,
        "price": `${flat.price}`,
        "numberOfRooms": `${flat.numberOfRooms}`,
        "description": `${flat.description}`,
        "email": `${flat.email}`,
        "phone_number": `${flat.phone_number}`,
        "zipCode": `${flat.zipCode}`,
        "city": `${flat.city}`,
        "address": `${flat.address}`,
        "forSale": `${flat.forSale}`
      },
      {
        responseType: 'text',
        headers: {
          'Authorization': 'Bearer' + ' ' + localStorage.getItem("token"),
          'Content-Type': 'application/json'
        }
      });
  }
  
  deleteFlat(flatID: String): Observable<Object> {
    return this.http.delete(this.apiRoot + 'flat/' + flatID, {
      responseType: 'text',
      headers: {
        'Authorization': 'Bearer' + ' ' + localStorage.getItem("token")
      }
    });
  }


  minusLastChar(str: String): String {
    if (str != null && str.length > 0) {
      str = str.substring(0, str.length - 1);
    }
    return str;
  }

  
  searchFlat(search: Flat, page: number): Observable<Object[]> {
    var maxprice: String;
    var numberOfRooms: String;
    var zipCode: String;
    var city: String;
    var forSale: String;
    var last: String;

    // Ha utolsó, akkor nem kell utána &, 

    if (search.price === "") {
      maxprice = "?";
    } else {
      maxprice = "/" + `${search.price}?`;
      last = maxprice;
    }
    if (search.numberOfRooms === "") {
      numberOfRooms = "";
    } else {
      numberOfRooms = "numberOfRooms=" + `${search.numberOfRooms}` + "&";
      last = numberOfRooms;
    }
    if (search.zipCode === "") {
      zipCode = "";
    } else {
      zipCode = "zipCode=" + `${search.zipCode}` + "&";
      last = zipCode;
    }
    if (search.city === "") {
      city = "";
    } else {
      city = "city=" + `${search.city}` + "&";
      last = city;
    }
    if (search.forSale === undefined) {
      forSale = "";
    } else {
      forSale = "forSale=" + `${search.forSale}`;
      last = forSale;
    }

    // Utolsó karakter leválasztása
    if (last === numberOfRooms) {
      numberOfRooms = this.minusLastChar(numberOfRooms);
    }
    if (last === zipCode) {
      zipCode = this.minusLastChar(zipCode);
    }
    if (last === city) {
      city === this.minusLastChar(city);
    }
    if (last === maxprice) {
      maxprice = this.minusLastChar(maxprice);
    }
    console.log(this.apiRoot + 'flats/' + page + maxprice + numberOfRooms + zipCode + city + forSale);

      return this.http.get<Object[]>(this.apiRoot + 'flats/' + page + maxprice + numberOfRooms + zipCode + city + forSale);
  }

}
