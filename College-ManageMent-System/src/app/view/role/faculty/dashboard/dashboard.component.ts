import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { FacultyRes } from '../../../models/response_dto/faculty-res';

const STORAGE_KEY = 'staff_data';

const DEFAULT_STAFF: FacultyRes[] = [
  { staffId: 1, staffName: 'Aabha',   departmentId: 3, salary: 50000 },
  { staffId: 2, staffName: 'Gayathri', departmentId: 2, salary: 5000000 },
  { staffId: 3, staffName: 'Disha',    departmentId: 4, salary: 45000 }
];

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {

  staffList: FacultyRes[] = [];
  loading = true;
  error = '';

  ngOnInit(): void {
    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      this.staffList = saved ? JSON.parse(saved) : DEFAULT_STAFF;
      if (!saved) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(DEFAULT_STAFF));
      }
    } catch {
      this.staffList = DEFAULT_STAFF;
    }
    this.loading = false;
  }

  get totalStaff(): number {
    return this.staffList.length;
  }

  get totalDepartments(): number {
    return new Set(this.staffList.map(s => s.departmentId)).size;
  }

  get avgSalary(): number {
    if (!this.staffList.length) return 0;
    return this.staffList.reduce((sum, s) => sum + s.salary, 0) / this.staffList.length;
  }

  get highestSalary(): number {
    if (!this.staffList.length) return 0;
    return Math.max(...this.staffList.map(s => s.salary));
  }
}
