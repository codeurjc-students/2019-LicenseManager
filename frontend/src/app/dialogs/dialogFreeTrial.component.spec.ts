
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatSnackBar, MatDialogRef, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material';
import { Router } from '@angular/router';
import { DOMHelper } from 'src/testing/dom-helper';
import { DialogFreeTrial } from './dialogFreeTrial.component';
import { MatDialogRefMock } from '../mock/MatDialogRefMock.component';
import { MatDialogMock } from '../mock/MatDialogMock.component';

describe('DialogFreeTrial', () => {
  let component: DialogFreeTrial;
  let fixture: ComponentFixture<DialogFreeTrial>;
  let routerMock:any;
  let domHelper:DOMHelper<DialogFreeTrial>;
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
      declarations: [ DialogFreeTrial ],
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
    fixture = TestBed.createComponent(DialogFreeTrial);
    component = fixture.componentInstance;
    domHelper = new DOMHelper(fixture);
  });


  describe("Page creation-display", () => {
      it("should create", () => {
          fixture.detectChanges();
          expect(component).toBeTruthy();


      })
  })


  describe("Methods", () => {

    it("should call yes()", () => {
        spyOn(component.dialogRef,"close");
        component.valid=true;
        component.typeSelected="M";
        fixture.detectChanges();
        domHelper.clickButtonById("#yes-button");
        expect(component.dialogRef.close).toHaveBeenCalledWith(component.resp);
    })

    it("should call closeDialog()", () => {
        spyOn(component.dialogRef,"close")
        fixture.detectChanges();
        domHelper.clickButtonById("#close-icon");
        expect(component.dialogRef.close).toHaveBeenCalledWith(null);
    })


    it("should execute setters of card-form", () => {
        fixture.detectChanges();
        component.setValid(true);
        expect(component.valid).toBe(true);
        component.setToken("tok");
        expect(component.token).toBe("tok");
        let any:any[];
        component.setParams(any);
        expect(component.params).toBe(any);
    })

  })



  
});
