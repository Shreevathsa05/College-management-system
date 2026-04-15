import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { FacultyService } from '../../../services/faculty/faculty.service';
import { FacultyRes } from '../../../models/response_dto/faculty-res';

@Component({
  selector: 'app-profiles',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profiles.component.html'
})
export class ProfilesComponent implements OnInit {

  staffList: FacultyRes[] = [];
  loading = true;
  error = '';

  searchId: number | null = null;
  selectedStaff: FacultyRes | null = null;
  searchError = '';

  constructor(private facultyService: FacultyService) {}

  ngOnInit(): void {
    this.facultyService.getFacultyList().subscribe({
      next: (data: FacultyRes[]) => {
        this.staffList = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Could not load profiles';
        this.loading = false;
      }
    });
  }

  searchById() {
    if (!this.searchId) {
      this.searchError = 'Enter ID';
      return;
    }

    this.facultyService.getFaculty(this.searchId).subscribe({
      next: (data: FacultyRes) => {
        this.selectedStaff = data;
      },
      error: () => {
        this.searchError = 'Not found';
      }
    });
  }

  selectProfile(staff: FacultyRes) {
    this.selectedStaff = staff;
    this.searchId = staff.staffId;   // ✅ FIXED (was id)
  }
}
