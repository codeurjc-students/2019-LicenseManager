import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogComponent } from './catalog.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import {  NgxPaginationModule } from 'ngx-pagination';
import { ProductService } from '../product/product.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Product } from '../product/product.model';

describe('CatalogComponent', () => {
  let component: CatalogComponent;
  let fixture: ComponentFixture<CatalogComponent>;

  beforeEach(async(() => {
    class ProductServiceStub{

      getProducts(){
        let prod:Product = {name: "Prod1", licenses:[], typeSubs:[],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:null,sku:null, active:true, trialDays:9, mode:"Both"};
        let array=[];
        array.push(prod);
        let resp =  Observable.of(array);
        return resp;
      }
    }

    const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

    TestBed.configureTestingModule({
      imports: [ NgxPaginationModule],
      declarations: [ CatalogComponent ],
      schemas:[NO_ERRORS_SCHEMA],
      providers:[
        {provide: ProductService, useClass:ProductServiceStub},
        { provide: Router,useValue: routerSpy},

      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CatalogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });




});
