import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacultyNavbarComponent } from './faculty-navbar.component';

describe('FacultyNavbarComponent', () => {
  let component: FacultyNavbarComponent;
  let fixture: ComponentFixture<FacultyNavbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FacultyNavbarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacultyNavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
