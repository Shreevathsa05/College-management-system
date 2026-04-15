import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { FacultyService } from '../../../../services/faculty/faculty.service';
import { FacultyRes } from '../../../../models/response_dto/faculty-res';
import { FacultyReq } from '../../../../models/request_dto/faculty-req';

@Component({
  selector: 'app-staff-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './staff-list.component.html'
})
export class StaffListComponent implements OnInit {

  staffList: FacultyRes[] = [];
  filteredList: FacultyRes[] = [];

  loading = true;
  error = '';
  successMsg = '';

  // Filters
  searchName = '';
  filterDeptId: number | null = null;
  filterMinSalary: number | null = null;

  // Modal
  showModal = false;
  isEditMode = false;
  editingId: number | null = null;
  modalError = '';

  // Delete Modal
  showDeleteModal = false;
  deletingId: number | null = null;
  deletingName = '';

  // Form
  formData: FacultyReq = {
    staffName: '',
    departmentId: 0,
    salary: 0
  };

  constructor(private facultyService: FacultyService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll() {
    this.loading = true;
    this.facultyService.getFacultyList().subscribe({
      next: (data: FacultyRes[]) => {
        this.staffList = data;
        this.filteredList = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load staff';
        this.loading = false;
      }
    });
  }

  // ================= SEARCH =================
  searchByNameFromServer() {
    if (!this.searchName.trim()) {
      this.filteredList = this.staffList;
      return;
    }

    this.filteredList = this.staffList.filter(s =>
      s.staffName.toLowerCase().includes(this.searchName.toLowerCase())
    );
  }

  // ================= FILTER =================
  filterByDept() {
    if (!this.filterDeptId) return;

    this.filteredList = this.staffList.filter(
      s => s.departmentId === this.filterDeptId
    );
  }

  filterBySalary() {
    if (!this.filterMinSalary) return;

    this.filteredList = this.staffList.filter(
      s => s.salary >= this.filterMinSalary!
    );
  }

  clearFilters() {
    this.searchName = '';
    this.filterDeptId = null;
    this.filterMinSalary = null;
    this.filteredList = this.staffList;
  }

  // ================= ADD =================
  openAddModal() {
    this.isEditMode = false;
    this.formData = { staffName: '', departmentId: 0, salary: 0 };
    this.showModal = true;
  }

  // ================= EDIT =================
  openEditModal(staff: FacultyRes) {
    this.isEditMode = true;
    this.editingId = staff.staffId;

    this.formData = {
      staffName: staff.staffName,
      departmentId: staff.departmentId,
      salary: staff.salary
    };

    this.showModal = true;
  }

  // ================= SAVE =================
  saveStaff() {
    if (!this.formData.staffName || !this.formData.departmentId || !this.formData.salary) {
      this.modalError = 'All fields are required';
      return;
    }

    if (this.isEditMode && this.editingId) {
      this.facultyService.updateFaculty(this.editingId, this.formData).subscribe({
        next: () => {
          this.successMsg = 'Updated successfully';
          this.loadAll();
        }
      });
    } else {
      this.facultyService.addFaculty(this.formData).subscribe({
        next: () => {
          this.successMsg = 'Added successfully';
          this.loadAll();
        }
      });
    }

    this.showModal = false;
  }

  // ================= DELETE =================
  openDeleteModal(staff: FacultyRes) {
    this.deletingId = staff.staffId;
    this.deletingName = staff.staffName;
    this.showDeleteModal = true;
  }

  confirmDelete() {
    if (!this.deletingId) return;

    this.facultyService.deleteFaculty(this.deletingId).subscribe({
      next: () => {
        this.successMsg = 'Deleted successfully';
        this.loadAll();
      }
    });

    this.showDeleteModal = false;
  }
}
