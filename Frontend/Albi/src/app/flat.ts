export class Flat {
  _id: String;
  userID: String;
  price: String;
  numberOfRooms: String;
  description: String;
  email: String;
  phone_number: String;
  address: String;
  hasAttachment: Boolean;
  imgPath: String[];


  static fromJSON(data: any) {
    return Object.assign(new this, data);
  }
}
