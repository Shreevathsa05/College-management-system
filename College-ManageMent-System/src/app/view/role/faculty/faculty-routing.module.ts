import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { ProfilesComponent } from './profiles/profiles.component';
import { StaffListComponent } from './staff/staff-list/staff-list.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'profiles',  component: ProfilesComponent },  // ✅ FIXED: was 'profile', navbar links to 'profiles'
      { path: 'staff',     component: StaffListComponent },
      { path: '',          redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FacultyRoutingModule {}
