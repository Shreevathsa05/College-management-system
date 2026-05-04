import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/layout/header/header.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { LandingComponent } from './components/layout/landing/landing.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NavSidebarComponent } from './components/layout/nav-sidebar/nav-sidebar.component';



@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    LandingComponent,
    NavSidebarComponent,
  ],
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    LandingComponent,
    NavSidebarComponent
  ]
})
export class CoreModule { }
