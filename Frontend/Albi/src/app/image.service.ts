import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Image } from './image';



const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable()
export class ImageService {

  apiRoot: string = 'http://localhost:3000/';


  constructor(private http: HttpClient) { }


  // Egy adott kép lekérése
  getFlatImage(filename: String): Observable<Blob> {
    return this.http.get(this.apiRoot + filename, { responseType: 'blob' });
  }

  // Adott ID-jű kép részleteinek lekérése
  getFlatImageDetails(imgUrl: String): Observable<Image[]> {
    return this.http.get<Image[]>(this.apiRoot + 'image/' + imgUrl);
  }


  // Egy adott hirdetés képeinek lekérése
  getFlatImageIDs(flatID: String): Observable<Image[]> {
    return this.http.get<Image[]>(this.apiRoot + 'flat/' + 'images/' + flatID);
  }

  // Kép feltöltése az adott hirdetéshez
  uploadImage() {

  }

  // Adott kép törlése
  deleteImage(imgID: String): Observable<String> {
    return this.http.delete(this.apiRoot + 'image/' + imgID, { responseType: 'text' });
  }
}
