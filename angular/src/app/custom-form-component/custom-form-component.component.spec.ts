import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomFormComponentComponent } from './custom-form-component.component';

describe('CustomFormComponentComponent', () => {
  let component: CustomFormComponentComponent;
  let fixture: ComponentFixture<CustomFormComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomFormComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomFormComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
