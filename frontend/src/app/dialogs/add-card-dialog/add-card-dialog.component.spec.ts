
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatSnackBar, MatDialogRef, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material';
import { Router } from '@angular/router';
import { DOMHelper } from 'src/testing/dom-helper';
import { AddCardDialogComponent } from './add-card-dialog.component';
import { MatDialogRefMock } from '../../mock/MatDialogRefMock.component';
import { MatDialogMock } from '../../mock/MatDialogMock.component';

describe('AddCardDialogComponent', () => {
  let component: AddCardDialogComponent;
  let fixture: ComponentFixture<AddCardDialogComponent>;
  let routerMock:any;
  let domHelper:DOMHelper<AddCardDialogComponent>;
  let dialogRefMock:any;

  afterEach(()=>{
    fixture.destroy();
  })
  
  
  beforeEach(async(() => {
    dialogRefMock = jasmine.createSpyObj("DialogRef",["open","close"]);

    dialogRefMock.open.and.returnValue(true);
    dialogRefMock.close.and.returnValue(true);


    routerMock = jasmine.createSpyObj('Router', ['navigate']);


    TestBed.configureTestingModule({
      declarations: [ AddCardDialogComponent ],
      imports: [ MatDialogModule],

      schemas:[NO_ERRORS_SCHEMA],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {checkbox:true, noButton:true} },
        {provide:MatDialogRef, useClass:MatDialogRefMock},
        {provide:MatDialog, useClass:MatDialogMock},
        {provide:MatSnackBar},
        { provide: Router,useValue: routerMock},


      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCardDialogComponent);
    component = fixture.componentInstance;
    domHelper = new DOMHelper(fixture);
  });


  describe("Page creation-display", () => {
      it("should create", () => {
          fixture.detectChanges();


      })
  })


  describe("Methods", () => {

    it("should call yes()", () => {
        spyOn(component.dialogRef,"close");
        component.cardAdded=true;
        fixture.detectChanges();
        domHelper.clickButtonById("#yes-button");
        expect(component.dialogRef.close).toHaveBeenCalledWith(true);
    })

    it("should call closeDialog()", () => {
        spyOn(component.dialogRef,"close")
        fixture.detectChanges();
        domHelper.clickButtonById("#close-icon");
        expect(component.dialogRef.close).toHaveBeenCalledWith(false);
    })

    it("should execute cardAddedHandler", () => {
        fixture.detectChanges();
        component.cardAddedHandler(true);
        expect(component.cardAdded).toBe(true);
    })

  })



  
});
