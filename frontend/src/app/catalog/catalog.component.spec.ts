import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogComponent } from './catalog.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import {  NgxPaginationModule } from 'ngx-pagination';
import { ProductService } from '../product/product.service';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Product } from '../product/product.model';
import {DOMHelper} from '../../testing/dom-helper'

describe('CatalogComponent', () => {
  let component: CatalogComponent;
  let fixture: ComponentFixture<CatalogComponent>;
  let productServiceMock:any;
  let domHelper:DOMHelper<CatalogComponent>;
  let routerMock:any;
  let prod:Product;
  let products:Product[] = [];

  afterEach(async() => {
    products=[];
  })

  beforeEach(async(() => {

    //MOCKS
    let plansPrices:{[name:string]:number} = {["M"]:2, ["D"]:0.5};
    let plansPrices2:{[name:string]:number} = {["L"]:10};

    prod = {name: "Prod1", licenses:[], typeSubs:["D","M"],photoAvailable:true,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices,sku:null, active:true, trialDays:9, mode:"Both"};
    let prod2:Product = {name: "Prod2", licenses:[], typeSubs:["L"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices2,sku:null, active:true, trialDays:9, mode:"Both"};

    products.push(prod);
    products.push(prod2);

     routerMock= jasmine.createSpyObj('Router', ['navigate']);

    productServiceMock = jasmine.createSpyObj("ProductService", ["getProducts","getProductSearch"]);
    productServiceMock.getProducts.and.returnValue(of(products));
    TestBed.configureTestingModule({
      imports: [ NgxPaginationModule],
      declarations: [ CatalogComponent ],
      schemas:[NO_ERRORS_SCHEMA],
      providers:[
        {provide: ProductService, useValue:productServiceMock},
        { provide: Router,useValue: routerMock},

      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CatalogComponent);
    component = fixture.componentInstance;
    domHelper= new DOMHelper<CatalogComponent>(fixture);
  });

  describe('Page creation-display',()=> {

    it('should create', () => {
      fixture.detectChanges();
      expect(component).toBeTruthy();
    });

    it('should initialize with elements on screen', () => {


      productServiceMock.getProducts.and.returnValue(of(products));

      fixture.detectChanges();

      expect(component.products.length).toBe(2);
      expect(component.products[0].name).toBe("Prod1");

      component.products.forEach(product => {
        console.log(product);
        let container = domHelper.checkExists("#productContainer"+product.name);
        expect(container).toBeTruthy();

        if(product.photoAvailable){
          expect(domHelper.checkExists("#photo"+product.name)).toBeTruthy();
        }

        product.typeSubs.forEach(type => {
          expect(domHelper.getText("#typeLic"+product.name)).toContain(type);
        })
      })
  })

  it('should show 2 products matching with the search=Prod', () => {
    let searchInput = "Prod"
    productServiceMock.getProductSearch.withArgs(searchInput).and.returnValue(of(products))  
    component.searchInputTerm=searchInput;
    fixture.detectChanges();

    component.searchProduct();
    expect(component.products.length).toBe(2);

  })

  it('should show 1 product matching with the search=Prod1', () => {
    let searchInput = "Prod1";
    let array:Product[] = [];
    array.push(prod);
    productServiceMock.getProductSearch.withArgs(searchInput).and.returnValue(of(array))  
    component.searchInputTerm=searchInput;
    fixture.detectChanges();

    component.searchProduct();
    expect(component.products.length).toBe(1);

  })

})

describe("Navigation", () => {

  it('should call to goToProduct', () => {
    spyOn(component,"goToProduct");
    fixture.detectChanges();

    component.products.forEach(product => {
      let titleProd = domHelper.getObject("#link"+product.name);
      titleProd.nativeElement.click();
    });
    expect(component.goToProduct).toHaveBeenCalledTimes(component.products.length);

  })

  it('should navigate to catalogProductComponent', () => {
    fixture.detectChanges();
    component.products.forEach(product => {
      component.goToProduct(product.name);
      expect(routerMock.navigate).toHaveBeenCalledWith(['/products/',product.name]);
    });
  })

})

describe("Error handling", () => {

  it('should console.log(error) for getProducts fail',() =>{
    let error = new Error();
    productServiceMock.getProducts.and.returnValue(Observable.create(observer => {observer.error(error)}));
    console.log = jasmine.createSpy("log");

    fixture.detectChanges();

    expect(console.log).toHaveBeenCalledWith(error);
  })

  it('should console.log(error) for getProductSearch fail',() =>{
    let error = new Error();
    productServiceMock.getProductSearch.and.returnValue(Observable.create(observer => {observer.error(error)}));
    console.log = jasmine.createSpy("log");

    fixture.detectChanges();

    component.searchProduct();

    expect(console.log).toHaveBeenCalledWith(error);
  })

})
  




});
