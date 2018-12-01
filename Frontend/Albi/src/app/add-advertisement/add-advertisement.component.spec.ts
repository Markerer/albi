import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAdvertisementComponent } from './add-advertisement.component';
import { ImageService } from '../image.service';
import { UserService } from '../user.service';
import { MainService } from '../main.service';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';

describe('AddAdvertisementComponent', () => {
  let component: AddAdvertisementComponent;
  let fixture: ComponentFixture<AddAdvertisementComponent>;

  it('#advertisementCreated should be true after creating an advertisement', () => {
    /*var fb: FormBuilder = new FormBuilder();
    var router: Router;
    var mainService: MainService;
    var imageService: ImageService;
    var userService: UserService;
    const comp = new AddAdvertisementComponent(fb, router, mainService, imageService, userService);*/
    expect(component.advertisementCreated).toBe(false, 'when the flat is not created yet');
    component.createFlat(30000, 3, "Kiváló ház", "t@t.com", "1234545", 2222, "Aggtelek", "Mészkő utca 1.", "underlease");
    //expect(comp.advertisementCreated).toBe(true, 'when the flat is created already');

    expect(component.createdFlat.price).toMatch('30000', 'after created');
    expect(component.createdFlat.email).toMatch('t@t.com', 'after created');
  });
  /*
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddAdvertisementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddAdvertisementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });*/
});
