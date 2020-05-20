/*
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import { MatLabel, MatFormFieldModule, MatInputModule, MatCardModule, MatCardActions, MatSnackBar, MatDialog } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterService } from './register.service';
import { LoginService } from '../login/login.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async(() => {

    const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

    TestBed.configureTestingModule({
      imports: [MatFormFieldModule, MatInputModule , MatCardModule, FormsModule, ReactiveFormsModule,BrowserAnimationsModule],
      declarations: [ RegisterComponent],
      providers:[
        { provide: Router,useValue: routerSpy},
        { provide:MatSnackBar},
        { provide: RegisterService},
        { provide: LoginService},
        { provide: MatDialog},


      ], 
      schemas:[NO_ERRORS_SCHEMA],

    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
*/