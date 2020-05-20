/*
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CardFormComponent } from './card-form.component';
import { MatIconModule, MatCardModule, MatDialog } from '@angular/material';
import { LoginService } from '../../login/login.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AppService } from '../../app.service';
import { UserProfileComponent } from '../userProfile.component';
import { UserProfileService } from '../userProfile.service';
//import { AppServiceStub } from '../../mock/app-service-stub.component'
import { Observable } from 'rxjs';

describe('CardFormComponent', () => {
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

  class AppServiceStub{

    getPublicStripeKey(){
      let resp =  Observable.of("s");
        return resp;
    }

    getAppName(){
        return "Testing"
    }
    
}

  let component: CardFormComponent;
  let fixture: ComponentFixture<CardFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ MatIconModule, MatCardModule ],
      declarations: [ CardFormComponent ],
      providers:[
        { provide: MatDialog },
        { provide: LoginService },
        { provide: HttpClient },
        { provide: Router,useValue: routerSpy},
        { provide: AppService, useClass:AppServiceStub },
        { provide: UserProfileService },
        { provide: UserProfileComponent },
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  
  it('should create', async() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
        expect(component).toBeTruthy();
       // console.log(component.token)
    });
  });
});


*/