import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Image } from './image';
import { AppSettings } from './appsettings';


@Injectable()
export class ImageService {


  constructor(private http: HttpClient) { }


  // Egy adott kép lekérése
  getFlatImage(filename: String): Observable<Blob> {
    return this.http.get(AppSettings.API_ROOT + filename, { responseType: 'blob' });
  }

  // Adott ID-jű kép részleteinek lekérése
  getFlatImageDetails(imgUrl: String): Observable<Image[]> {
    return this.http.get<Image[]>(AppSettings.API_ROOT + 'image/' + imgUrl);
  }


  // Egy adott hirdetés képeinek lekérése
  getFlatImageIDs(flatID: String): Observable<Image[]> {
    return this.http.get<Image[]>(AppSettings.API_ROOT + 'flat/' + 'images/' + flatID);
  }

  // Kép feltöltése az adott hirdetéshez
  uploadImage(uploadData: FormData, flatID: String): Observable<Object> {
    return this.http.post(AppSettings.API_ROOT + 'flat/upload/' + flatID, uploadData,
      {
        headers: {
          'Authorization': 'Bearer' + ' ' + localStorage.getItem("token")
          //'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
  }

  // Adott kép törlése
  deleteImage(imgID: String): Observable<Object> {
    return this.http.delete(AppSettings.API_ROOT + 'image/' + imgID,
      {
        responseType: 'text',
        headers: {
          'Authorization': 'Bearer' + ' ' + localStorage.getItem("token"),
          'Content-Type': 'application/json'
        }
      });
  }
}
