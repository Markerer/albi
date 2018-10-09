import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { User } from './user';


@Injectable()
export class DataService {

  user: User;

  private dataSource = new BehaviorSubject(this.user);
  currentData = this.dataSource.asObservable();

  constructor() { }

  changeData(loggedUser: User): void {
    this.dataSource.next(loggedUser);
  }

}
