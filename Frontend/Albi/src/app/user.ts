export class User {
  _id: String;
  username: String;
  password: String;
  phone_number: String;
  address: String;
  email: String;

  static fromJSON(data: any) {
    return Object.assign(new this, data);
  }
}
