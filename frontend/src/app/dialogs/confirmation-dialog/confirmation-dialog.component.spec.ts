
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatSnackBar, MatDialogRef, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material';
import { Router } from '@angular/router';
import { DOMHelper } from 'src/testing/dom-helper';
import { User } from 'src/app/login/login.service';
import { ConfirmationDialogComponent } from './confirmation-dialog.component';
import { MatDialogRefMock } from '../../mock/MatDialogRefMock.component';
import { MatDialogMock } from '../../mock/MatDialogMock.component';

describe('ConfirmatonDialogComponent', () => {
  let component: ConfirmationDialogComponent;
  let fixture: ComponentFixture<ConfirmationDialogComponent>;
  let routerMock:any;
  let domHelper:DOMHelper<ConfirmationDialogComponent>;
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
      declarations: [ ConfirmationDialogComponent ],
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
    fixture = TestBed.createComponent(ConfirmationDialogComponent);
    component = fixture.componentInstance;
    domHelper = new DOMHelper(fixture);
  });


  describe("Page creation-display", () => {
      it("should create", () => {
          fixture.detectChanges();
          expect(component.checkboxStatus).toBe(false);
          expect(component.noButton).toBe(true);

      })
  })


  describe("Methods", () => {

    it("should call yes()", () => {
        fixture.detectChanges();
        expect(component.exit[0]).toBe(undefined);
        domHelper.clickButtonById("#yes-button");
        expect(component.exit[0]).toBe(true);
    })

    it("should call closeDialog()", () => {
        spyOn(component.dialogRef,"close")
        fixture.detectChanges();
        expect(component.exit[0]).toBe(undefined);
        domHelper.clickButtonById("#close-icon");
        expect(component.dialogRef.close).toHaveBeenCalled();
    })

    it("should call no()", () => {
        spyOn(component.dialogRef,"close")
        fixture.detectChanges();
        expect(component.exit[0]).toBe(undefined);
        domHelper.clickButtonById("#no-button");
        expect(component.exit[0]).toBe(false);
        expect(component.dialogRef.close).toHaveBeenCalled();
    })

  })



  
});
