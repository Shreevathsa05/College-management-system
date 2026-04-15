import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { FacultyService } from '../../../services/faculty/faculty.service';
import { FacultyRes } from '../../../models/response_dto/faculty-res';

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

  constructor(private facultyService: FacultyService) {}

  ngOnInit(): void {
    this.facultyService.getFacultyList().subscribe({
      next: (data: FacultyRes[]) => {
        this.staffList = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Backend not reachable';
        this.loading = false;
      }
    });
  }

  // ✅ ADD THESE (THIS FIXES ALL ERRORS)

  get totalStaff(): number {
    return this.staffList.length;
  }

  get totalDepartments(): number {
    return new Set(this.staffList.map(s => s.departmentId)).size;
  }

  get avgSalary(): number {
    if (!this.staffList.length) return 0;

    return Number(
      (
        this.staffList.reduce((sum, s) => sum + s.salary, 0) /
        this.staffList.length
      ).toFixed(2)
    );
  }

  get highestSalary(): number {
    if (!this.staffList.length) return 0;

    return Math.max(...this.staffList.map(s => s.salary));
  }
}
