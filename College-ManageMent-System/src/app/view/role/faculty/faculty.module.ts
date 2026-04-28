import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FacultyRoutingModule } from './faculty-routing.module';

import { DashboardComponent } from './dashboard/dashboard.component';
import { ProfilesComponent } from './profiles/profiles.component';
import { StaffListComponent } from './staff/staff-list/staff-list.component';


@NgModule({
  imports: [
    CommonModule,
    FacultyRoutingModule,

    // ✅ standalone components go here
    DashboardComponent,
    ProfilesComponent,
    StaffListComponent
  ],
  declarations: [

  ]
})
export class FacultyModule {}
