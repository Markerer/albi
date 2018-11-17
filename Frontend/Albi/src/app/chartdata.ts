declare var require: any;

export class ChartData {

  flatid: String;
  counter: Number;
  date: String;

  public dateToString(date: Date) {
    this.date += date.getFullYear.toString();
    this.date += ".";
    this.date += date.getMonth.toString();
    this.date += ".";
    this.date += date.getDay.toString();
    this.date += ".";
    console.log(this.date);
  }

  public setTodayDate(): void {
    var dateFormat = require('dateformat');
    var today = new Date();
    this.date = dateFormat(today, "yyyy.mm.dd.").toString();
    console.log(this.date);
  }
}
