import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { Flat } from '../flat';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  flats: Flat[];

  constructor(private mainService: MainService) { }

  ngOnInit() {
  }

  getFlats(): void {
    this.mainService.getMainScreen().subscribe(data => { this.flats = data; });
  }


}
