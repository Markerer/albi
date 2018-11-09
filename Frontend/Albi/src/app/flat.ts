import { Image } from "./image";

export class Flat {
  _id: String;
  userID: String;
  price: String;
  numberOfRooms: String;
  description: String;
  email: String;
  phone_number: String;
  zipcode: String;
  city: String;
  address: String;
  forSale: Boolean;
  images: Image[];
  firstImage: Image;
  noImageFound: boolean = true;


  constructor() {
    this.images = [];
    this.firstImage = new Image();
  }
}
