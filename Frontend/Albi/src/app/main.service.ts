import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Flat } from './flat';
import { ChartData } from './chartdata';
import { AppSettings } from './appsettings';


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable()
export class MainService {

  constructor(private http: HttpClient) { }

  // Fő oldalon megjelenő hirdetések lekérése
  getMainScreen(page: number): Observable<Object[]> {
    return this.http.get<Object[]>(AppSettings.API_ROOT + 'api/' + 'main/' + page);
  }

  // Egy lakás lekérése
  getFlatByID(flatID: String): Observable<Flat> {
    return this.http.get<Flat>(AppSettings.API_ROOT + 'flat/' + flatID);
  }
  
  // Egy hirdetéshez tartozó megtekintési adatok lekérése
  getAdvertisementStats(flatID: String): Observable<ChartData[]> {
    return this.http.get<ChartData[]>(AppSettings.API_ROOT + 'flat/' + 'dates/' + flatID);
  }

  // Egy hirdetés megtekintési számának növelése
  addViewingToAdvertisement(flatID: String, date: String): Observable<ChartData>{
    return this.http.post<ChartData>(AppSettings.API_ROOT + 'date/' + flatID,
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
    return this.http.get<Flat[]>(AppSettings.API_ROOT + 'user/flats/' + userID);
  }

  // Új hirdetés feladása
  addAdvertisement(flat: Flat): Observable<Flat> {
    return this.http.post<Flat>(AppSettings.API_ROOT + 'addflat/' + flat.userID,
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
    return this.http.put(AppSettings.API_ROOT + 'flats/',
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
    return this.http.delete(AppSettings.API_ROOT + 'flat/' + flatID, {
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
    //console.log(this.apiRoot + 'flats/' + page + maxprice + numberOfRooms + zipCode + city + forSale);

    return this.http.get<Object[]>(AppSettings.API_ROOT + 'flats/' + page + maxprice + numberOfRooms + zipCode + city + forSale);
  }

}
