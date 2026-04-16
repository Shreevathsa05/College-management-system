import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { FacultyRes } from '../../../models/response_dto/faculty-res';

const STORAGE_KEY = 'staff_data';

const DEFAULT_STAFF: FacultyRes[] = [
  { staffId: 3, staffName: 'Aabha',    departmentId: 3, salary: 50000 },
  { staffId: 6, staffName: 'Gayathri', departmentId: 2, salary: 5000000 },
  { staffId: 2, staffName: 'Disha',    departmentId: 4, salary: 45000 }
];

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

  ngOnInit(): void {
    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      this.staffList = saved ? JSON.parse(saved) : DEFAULT_STAFF;
    } catch {
      this.staffList = DEFAULT_STAFF;
    }
    this.loading = false;
  }

  searchById() {
    this.searchError = '';
    if (!this.searchId) {
      this.searchError = 'Please enter a valid Staff ID';
      return;
    }
    const found = this.staffList.find(s => s.staffId === Number(this.searchId));
    if (found) {
      this.selectedStaff = found;
    } else {
      this.searchError = `No staff found with ID ${this.searchId}`;
      this.selectedStaff = null;
    }
  }

  selectProfile(staff: FacultyRes) {
    this.selectedStaff = staff;
    this.searchId = staff.staffId;
    this.searchError = '';
  }
}
