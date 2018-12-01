declare var require: any;

export class ChartData {

  _id: String;
  flatID: String;
  counter: Number;
  date: String;

  public dateToString(date: Date) {
    this.date += date.getFullYear.toString();
    this.date += ".";
    this.date += date.getMonth.toString();
    this.date += ".";
    this.date += date.getDay.toString();
   //
   // .log(this.date);
  }

  public setTodayDate(): void {
    var dateFormat = require('dateformat');
    var today = new Date();
    this.date = dateFormat(today, "yyyy.mm.dd").toString();
    //console.log(this.date);
  }
}
