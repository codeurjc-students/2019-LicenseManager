import { AdminDashboardComponent } from "./adminDashboard.component";
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NO_ERRORS_SCHEMA } from '@angular/core';

import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { LoginService } from '../login/login.service';
import { NgxPaginationModule } from 'ngx-pagination';
import { MatSnackBarModule, MatDialogModule, MatDialogRef } from '@angular/material';
import { ProductService } from '../product/product.service';
import { Product } from '../product/product.model';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DialogAddProductComponent } from '../dialogs/dialogAddProduct.component';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { DialogService } from '../dialogs/dialog.service';

describe('AdminDashboardComponent', () => {
    let component: AdminDashboardComponent;
    let fixture: ComponentFixture<AdminDashboardComponent>;
    let routerMock:any;
    let loginServiceMock:any;
    let productServiceMock:any;
    let dialogServiceMock:any;


    let prod:Product;
    let products:Product[] = [];

    
    afterEach(async() => {
        products=[];
    })

    beforeEach(async(() => {
      
      routerMock = jasmine.createSpyObj('Router', ['navigate']);
    
      productServiceMock = jasmine.createSpyObj("ProductServiceMock", ["getProducts","deleteProduct"])
      
      let plansPrices:{[name:string]:number} = {["M"]:2, ["D"]:0.5};
      let plansPrices2:{[name:string]:number} = {["L"]:10};
  
      prod = {name: "Prod1", licenses:[], typeSubs:["D","M"],photoAvailable:true,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices,sku:null, active:true, trialDays:9, mode:"Both"};
      let prod2:Product = {name: "Prod2", licenses:[], typeSubs:["L"],photoAvailable:false,description: "The description",webLink:"www.c.com",photoSrc:"",plansPrices:plansPrices2,sku:null, active:true, trialDays:9, mode:"Both"};
  
      products.push(prod);
      products.push(prod2);

      productServiceMock.getProducts.and.returnValue(of(products));

      dialogServiceMock = jasmine.createSpyObj("DialogService",["openAddProductDialog", "openEditProductDialog","openConfirmDialog"])

      TestBed.configureTestingModule({
        imports: [ BrowserAnimationsModule,MatSnackBarModule,NgxPaginationModule, MatDialogModule],

        declarations: [ AdminDashboardComponent,DialogAddProductComponent ],
        schemas:[NO_ERRORS_SCHEMA],
        providers: [
          {provide:LoginService, useValue:loginServiceMock},
          { provide: Router,useValue: routerMock},
          {provide:ProductService, useValue: productServiceMock},
          {provide: DialogService, useValue: dialogServiceMock}
  
        ],
      })
      .overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [DialogAddProductComponent] } })

      .compileComponents();
    }));
  
    beforeEach(() => {
      fixture = TestBed.createComponent(AdminDashboardComponent);
      component = fixture.componentInstance;
    });
  

    describe("Page creation-display",()=> {
        it('should create', () => {
            fixture.detectChanges();

            expect(component).toBeTruthy();
        });
    })

    describe("Error handling", () => {
        it('should console.log(error) when getProducts', () => {
            let error = new Error();
            productServiceMock.getProducts.and.returnValue(Observable.create(observer => {observer.error(error)}));
            console.log = jasmine.createSpy("log");

            fixture.detectChanges();

            expect(console.log).toHaveBeenCalledWith(error);
        });

        it("should fail opening the add product", () => {
            let error = new Error();

            console.log = jasmine.createSpy("log");

            dialogServiceMock.openAddProductDialog.and.returnValue({afterClosed: () =>Observable.create(observer => {observer.error(error)})});
            fixture.detectChanges();

            component.openAddProductDialog();
            expect(console.log).toHaveBeenCalledWith(error);

        })

        it("should fail deleting a product", () => {
            let res:any = [];
            res[0]=true;
            dialogServiceMock.openConfirmDialog.and.returnValue({afterClosed: () => of(res)});
            let error = new Error();

            productServiceMock.deleteProduct.withArgs(prod).and.returnValue(Observable.create(observer => {observer.error(error)}));

            console.log = jasmine.createSpy("log");

            fixture.detectChanges();
            component.deleteProduct(prod);

            expect(console.log).toHaveBeenCalledWith(error);
        })
    })

    describe("Actions", () => {
        it("should open the add product dialog", () => {
            dialogServiceMock.openAddProductDialog.and.returnValue({afterClosed: () => of(true)});
            spyOn(component.snackbar,"open");
            fixture.detectChanges();

            component.openAddProductDialog();

            expect(component.snackbar.open).toHaveBeenCalledTimes(1);
        })

        it("should open the edit product dialog", () => {
            dialogServiceMock.openEditProductDialog.withArgs(prod).and.returnValue({afterClosed: () => of(true)});
            spyOn(component.snackbar,"open");
            fixture.detectChanges();

            component.openEditProductDialog(prod);

            expect(component.snackbar.open).toHaveBeenCalledTimes(1);
        })

        it("should open the delete product dialog", () => {
            let res:any = [];
            res[0]=true;
            dialogServiceMock.openConfirmDialog.and.returnValue({afterClosed: () => of(res)});

            productServiceMock.deleteProduct.withArgs(prod).and.returnValue(of(true));
            spyOn(component.snackbar,"open");
            fixture.detectChanges();

            component.deleteProduct(prod);

            expect(component.snackbar.open).toHaveBeenCalledTimes(1);
        })

    })

  
  
  
  });