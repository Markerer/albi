import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MainService } from '../main.service';
import { User } from '../user';
import { Flat } from '../flat';
import { Subject } from 'rxjs/Subject';
import { debounceTime } from 'rxjs/operators';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ImageService } from '../image.service';
import { Image } from '../image';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Chart } from 'chart.js';
import { ChartData } from '../chartdata';
import { UserService } from '../user.service';
import { AppSettings } from '../appsettings';

declare var require: any;

@Component({
  selector: 'app-flat',
  templateUrl: './flat.component.html',
  styleUrls: ['./flat.component.scss']
})
export class FlatComponent implements OnInit {
  @ViewChild('mycanvas') mycanvas: ElementRef;

  undefinedUser: boolean = false;
  visitorMode: boolean = false;
  statisticsMode: boolean = false; 
  owner: boolean;


  flat: Flat;
  user: User;

  chart: any[];

  selectedFile: File;

  updateFlatForm: FormGroup;

  private _success = new Subject<string>();
  private _successUpload = new Subject<string>();
  successUpload: string;
  successMessage: string;
  closeResult: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private mainService: MainService,
    private fb: FormBuilder,
    private imageService: ImageService,
    private modalService: NgbModal,
    private userService: UserService)
  {  }

  ngOnInit() {

    if (this.userService.isLoggedIn()) {
      var temp = JSON.parse(localStorage.getItem("user"));
      this.user = temp;
     // console.log(this.user);
      this.undefinedUser = false;
      this.flat = new Flat();
      this.getFlat();
    }
    else {
      this.undefinedUser = true;
      this.router.navigate(['']);
      if (this.userService.isLoggedOut()) {
        this.userService.logout();
      }
    }

    //A sikeres üzenet
    this._success.subscribe((message) => this.successMessage = message);
    this._success.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successMessage = null);

    //A sikeres üzenet
    this._successUpload.subscribe((message) => this.successUpload = message);
    this._successUpload.pipe(
      debounceTime(5000)
    ).subscribe(() => this.successUpload = null);
  }

  private viewAd() {
    if (!this.owner) {
      var today: ChartData = new ChartData();
      today.setTodayDate();
      var _id = this.flat._id;
     // console.log(_id);
      this.mainService.addViewingToAdvertisement(_id, today.date).subscribe(response => console.log(response));
    }
  }


  ngOnDestroy() {
    this.flat = null;
  }

  public changeSuccessMessage(): void {
    this._success.next(`Your advertisement have been successfully updated.`);
  }

  public changeSuccessUploadMsg(): void {
    this._successUpload.next(`Your picture is being uploaded. Wait a few seconds, please.`);
  }

  navigateToMyAds(): void {
    this.router.navigate(['main/myadvertisements']);
  }

  getFlat(): void {
    //Az id kinyerése az URL címből.
    var id: String = this.activatedRoute.snapshot.paramMap.get('_id');
   // console.log(id);
    this.mainService.getFlatByID(id).subscribe(data => {
      this.flat = new Flat();
      this.flat = data;
      this.flat.images = [];
      this.flat.firstImage = new Image();
      this.getImageUrls(this.flat._id, this.flat);
     // console.log(this.flat);
      // Megnézzük, hogy saját hirdetésünk-e
      if (this.flat.userID === this.user._id) {
        this.owner = true;
      } else {
        this.owner = false;
      }

      this.updateFlatForm = this.fb.group({
        'price': [this.flat.price, null],
        'numberOfRooms': [this.flat.numberOfRooms, null],
        'description': [this.flat.description, null],
        'email': [this.flat.email, null],
        'phone_number': [this.flat.phone_number, null],
        'zipCode': [this.flat.zipCode, null],
        'city': [this.flat.city, null],
        'address': [this.flat.address, null],
        'type': ["", null]
      });
      this.viewAd();
    });
  }

  updateFlat(price: Number, numberOfRooms: Number, description: String, email: String, phone_number: String, zipCode: Number, city: String, address: String, type: String): void {
    if (!(price === undefined || price === null)) {
      this.flat.price = price.toString();
    }
    if (!(numberOfRooms === undefined || numberOfRooms === null)) {
      this.flat.numberOfRooms = numberOfRooms.toString();
    }
    if (!(description === "" || description === undefined)) {
      this.flat.description = description;
    }
    if (!(email === "" || email === undefined)) {
      this.flat.email = email;
    }
    if (!(phone_number === "" || phone_number === undefined)) {
      this.flat.phone_number = phone_number;
    }
    if (!(zipCode === undefined || zipCode === null)) {
      this.flat.zipCode = zipCode.toString();
    }
    if (!(city === "" || city === undefined)) {
      this.flat.city = city;
    }
    if (!(address === "" || address === undefined)) {
      this.flat.address = address;
    }
    if (type === "underlease") {
      this.flat.forSale = false;
    } else if (type === "sale") {
      this.flat.forSale = true;
    }

   // console.log(this.flat);


    this.mainService.updateFlat(this.flat).subscribe(
      msg => {
        console.log(msg);
      });
  }

  // A lakás képeinek (ID és fájlnév) beállítása
  getImageUrls(flatID: String, flat: Flat) {
    this.imageService.getFlatImageIDs(flatID).subscribe(data => {
      flat.images = [];
      for (let i = 0; i < data.length; i++) {
        var temp = new Image();
        temp = data[i];
        temp.filename = AppSettings.API_ROOT + data[i].filename;
        flat.images.push(temp);
      }
      flat.firstImage = new Image();
      if (flat.images[0] === undefined) {
        flat.noImageFound = true;
        flat.firstImage.filename = "assets/img/download.png";
      }
      else {
        flat.noImageFound = false;
        flat.firstImage.filename = flat.images[0].filename;
      }
    });
  }

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  uploadImage() {
    if (!(this.selectedFile === null || this.selectedFile === undefined)) {
      const uploadData = new FormData();
      uploadData.append('image', this.selectedFile, this.selectedFile.name);
      this.imageService.uploadImage(uploadData, this.flat._id).subscribe(object => {
      //  console.log(object);
        this.changeSuccessUploadMsg();
        this.getFlat();
      });
    }
  }

  deleteImage(imageID: String): void {
    this.imageService.deleteImage(imageID).subscribe(response => {
    //  console.log(response);
      this.getFlat();
    });
  }

  visitMode(): void {
    this.visitorMode = !this.visitorMode;
  }

  statMode(): void {
    this.statisticsMode = !this.statisticsMode;
    if (this.statisticsMode) {
      (this.mycanvas.nativeElement).style.display = "inline-block";
    } else {
      (this.mycanvas.nativeElement).style.display = "none";
    }
  }

  deleteAdvertisement(): void {
    for (let i of this.flat.images) {
      this.imageService.deleteImage(i._id).subscribe(response => console.log(response));
    }
    this.mainService.deleteFlat(this.flat._id).subscribe(response => {
     // console.log(response);
      this.navigateToMyAds();
    });
  }

  // A modal ablak kódja
  open(content, type) {
    if (type === 'sm') {

      this.modalService.open(content, { size: 'sm' }).result.then((result) => {
        this.closeResult = `Closed with: ${result}`;
      }, (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      });
    } else {
      this.modalService.open(content).result.then((result) => {
        this.closeResult = `Closed with: ${result}`;
        if (`${result}` === 'Delete') {
          //console.log('delete');
          this.deleteAdvertisement();
        }
      }, (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      });
    }
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  private getStats(): void {
    
    this.mainService.getAdvertisementStats(this.flat._id).subscribe(chartData => {
      this.loadChart(chartData);
    });
    this.statMode();
  }


  public loadChart(chartDatas: ChartData[]): void {
    
    let labels: String[] = [];
    let counts: Number[] = [];

    for (let i of chartDatas) {
      labels.push(i.date);
      counts.push(i.counter);
    }

    var context = (<HTMLCanvasElement>this.mycanvas.nativeElement).getContext('2d');

    this.chart = new Chart(context, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            data: counts,
            backgroundColor: "#3cba9f",
            fill: false,
            barPercentage: 0.5
          }
        ]
      },
      options: {
        legend: {
          display: false
        },
        scales: {
          xAxes: [{
            display: true
          }],
          yAxes: [{
            display: true,
            ticks: {
              beginAtZero: true,
              stepSize: 1
            }

          }],
        }
      }
    });
   

  }
}
