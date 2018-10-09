export class Flat {
  _id: String;
  flatname: String;
  username: String;
  email: String;
  phone_number: String;
  address: String;
  hasAttachment: boolean;
  __v: number;

  static fromJSON(data: any) {
    return Object.assign(new this, data);
  }
}
