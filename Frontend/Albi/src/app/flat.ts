import { Image } from './image';

export class Flat {
  _id: String;
  userID: String;
  price: String;
  numberOfRooms: String;
  description: String;
  email: String;
  phone_number: String;
  zipCode: String;
  city: String;
  address: String;
  forSale: Boolean;
  images: Image[];
  firstImage: Image;
  noImageFound = true;


  constructor() {
    this.images = [];
    this.firstImage = new Image();
  }
}
