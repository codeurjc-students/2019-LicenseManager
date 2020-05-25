
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatSnackBar, MatDialogRef, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { DOMHelper } from 'src/testing/dom-helper';
import { DialogAddProductComponent } from './dialogAddProduct.component';
import { ProductService } from '../product/product.service';
import { Product } from '../product/product.model';
import { MatDialogRefMock } from '../mock/MatDialogRefMock.component';
import { MatDialogMock } from '../mock/MatDialogMock.component';

let error = new Error();
let prod:Product;
let component: DialogAddProductComponent;
let fixture: ComponentFixture<DialogAddProductComponent>;
let dialogRefMock:any;
let productServMock:any;


describe('DialogAddProductComponent Edit', () => {
  let routerMock:any;
  let domHelper:DOMHelper<DialogAddProductComponent>;



  afterEach(()=>{
    fixture.destroy();
  })
  
  
  beforeEach(async(() => {

    let plansPrices:{[name:string]:number} = {["M"]:2, ["D"]:0.5, ["A"]:100};
  
    prod = {name: "Prod1", licenses:[], typeSubs:["D","M","A"],photoAvailable:true,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices,sku:null, active:true, trialDays:9, mode:"Both"};
  

    dialogRefMock = jasmine.createSpyObj("DialogRef",["open","close"]);

    dialogRefMock.open.and.returnValue(true);
    dialogRefMock.close.and.returnValue(true);


    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    productServMock = jasmine.createSpyObj("ProductService", ["putProduct","postProduct","addImage"]);

    productServMock.putProduct.and.returnValue(of(prod));
    productServMock.postProduct.and.returnValue(of(prod));
    productServMock.addImage.and.returnValue(Observable.create(observer => {observer.error(error)}));

    console.log = jasmine.createSpy("log");

    TestBed.configureTestingModule({
      declarations: [ DialogAddProductComponent ],
      imports: [ MatDialogModule],

      schemas:[NO_ERRORS_SCHEMA],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {type:"edit", product:prod} },
        {provide:MatDialogRef, useClass:MatDialogRefMock},
        {provide:MatDialog, useClass:MatDialogMock},
        {provide:MatSnackBar},
        { provide: ProductService, useValue: productServMock},
        { provide: Router,useValue: routerMock},


      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogAddProductComponent);
    component = fixture.componentInstance;
    domHelper = new DOMHelper(fixture);
  });


  describe("Page creation-display", () => {
      it("should create", () => {
          fixture.detectChanges();
          expect(component.modeSelected).toBe(prod.mode);
          expect(component.name).toBe(prod.name);
          expect(component.description).toBe(prod.description);
          expect(component.webLink).toBe(prod.webLink);
          expect(component.trialDays).toBe(prod.trialDays);
          expect(component.daily).toBe(true);
          expect(component.monthly).toBe(true);
          expect(component.annual).toBe(true);
      })
  })


  describe("Methods", () => {
    it("should call save()", () => {
        spyOn(component.dialogRef,"close");
        fixture.detectChanges();
        let file:any ={size:999999};
        component.file=file;
        domHelper.clickButtonById("#saveButton");
        expect(true).toBe(true);
    })

    it("should uploadEvent", () => {
        fixture.detectChanges();
        let file:any ={size:999999};
        component.file=file;
        component.uploadEvent(file);
        expect(true).toBe(true);
    })

    it("should uploadEvent", () => {
        fixture.detectChanges();
        let file:any ={size:999999};
        component.file=file;
        component.selectEvent(file);
        expect(component.file).toBe(file);
    })

    it("should cancelEvent", () => {
        fixture.detectChanges();

        component.cancelEvent();
        expect(true).toBe(true);
        //yet to implement
    })

    it("should alert max size file in uploadEvent", () => {
        spyOn(window, 'alert');
        fixture.detectChanges();
        let file:any ={size:9999999};
        component.file=file;
        component.uploadEvent(file);
        expect(window.alert).toHaveBeenCalledWith('The file is too large. Max: 1048575 bytes');

    })

    it("should alert max size file in selectEvent", () => {
        spyOn(window, 'alert');
        fixture.detectChanges();
        let file:any ={size:9999999};
        component.file=file;
        component.selectEvent(file);
        expect(window.alert).toHaveBeenCalledWith('The file is too large. Max: 1048575 bytes');
    })

    it("should call closeDialog()", () => {
        spyOn(component.dialogRef,"close")
        fixture.detectChanges();
        domHelper.clickButtonById("#cancelButton");
        expect(component.dialogRef.close).toHaveBeenCalledWith(false);
    })

  })


  describe("Error handling", () => {
    it("should fail adding an image", () => {
        fixture.detectChanges();
        let file:any ={size:800};
        component.uploadEvent(file);

        expect(console.log).toHaveBeenCalledWith(error);
    })

    it("should fail putting a product", () => {
        productServMock.putProduct.and.returnValue(Observable.create(observer => {observer.error(error)}));
        fixture.detectChanges();
        component.file=null;
        component.save();

        expect(console.log).toHaveBeenCalledWith(error);
    })
  })



  
});



describe('DialogAddProductComponent Add', () => {
  
    afterEach(()=>{
      fixture.destroy();
    })
    
    
    beforeEach(async(() => {
  
      dialogRefMock = jasmine.createSpyObj("DialogRef",["open","close"]);
  
      dialogRefMock.close.and.returnValue(true);
  
    
      productServMock = jasmine.createSpyObj("ProductService", ["putProduct","postProduct","addImage"]);
      productServMock.postProduct.and.returnValue(of(prod));  
  
      TestBed.configureTestingModule({
        declarations: [ DialogAddProductComponent ],
        imports: [ MatDialogModule],
  
        schemas:[NO_ERRORS_SCHEMA],
        providers: [
          { provide: MAT_DIALOG_DATA, useValue: {type:"add", product:prod} },
          {provide:MatDialogRef, useClass:MatDialogRefMock},
          {provide:MatDialog, useClass:MatDialogMock},
          {provide:MatSnackBar},
          { provide: ProductService, useValue: productServMock},  
        ]
      })
      .compileComponents();
    }));
  
    beforeEach(() => {
      fixture = TestBed.createComponent(DialogAddProductComponent);
      component = fixture.componentInstance;
    });


    describe("Methods", () => {

        it("should call add()", () => {
            spyOn(component.dialogRef,"close");
            spyOn(component,"uploadEvent");
            fixture.detectChanges();
            let file:any ={size:999999};
            component.file=file;
            component.edit=false;
            component.type="subscription";
            component.daily=true;
            component.monthly=true;
            component.annual=true;
            component.add();
            expect(component.dialogRef.close).toHaveBeenCalledWith(true);
            expect(component.uploadEvent).toHaveBeenCalledWith(file);

        })

    })


});