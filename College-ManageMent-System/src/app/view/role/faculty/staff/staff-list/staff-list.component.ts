import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { FacultyRes } from '../../../../models/response_dto/faculty-res';
import { FacultyReq } from '../../../../models/request_dto/faculty-req';

const STORAGE_KEY = 'staff_data';

const DEFAULT_STAFF: FacultyRes[] = [
  { staffId: 3, staffName: 'Aabha',    departmentId: 3, salary: 50000 },
  { staffId: 6, staffName: 'Gayathri', departmentId: 2, salary: 5000000 },
  { staffId: 2, staffName: 'Disha',    departmentId: 4, salary: 45000 }
];

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
  formData: FacultyReq = { staffName: '', departmentId: 0, salary: 0 };

  ngOnInit(): void {
    this.loadAll();
  }

  // ======= LOAD (from localStorage, fallback to defaults) =======
  loadAll() {
    this.loading = true;
    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      if (saved) {
        this.staffList = JSON.parse(saved);
      } else {
        this.staffList = [...DEFAULT_STAFF];
        this.saveToStorage();
      }
    } catch {
      this.staffList = [...DEFAULT_STAFF];
    }
    this.filteredList = [...this.staffList];
    this.loading = false;
  }

  private saveToStorage() {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(this.staffList));
    } catch { /* ignore */ }
  }

  // ======= SEARCH =======
  searchByNameFromServer() {
    if (!this.searchName.trim()) {
      this.filteredList = [...this.staffList];
      return;
    }
    this.filteredList = this.staffList.filter(s =>
      s.staffName.toLowerCase().includes(this.searchName.toLowerCase())
    );
  }

  // ======= FILTERS =======
  filterByDept() {
    if (this.filterDeptId === null || this.filterDeptId === undefined) {
      this.filteredList = [...this.staffList];
      return;
    }
    this.filteredList = this.staffList.filter(s => s.departmentId === Number(this.filterDeptId));
  }

  filterBySalary() {
    if (this.filterMinSalary === null || this.filterMinSalary === undefined) {
      this.filteredList = [...this.staffList];
      return;
    }
    const min = Number(this.filterMinSalary);
    this.filteredList = this.staffList.filter(s => Number(s.salary) >= min);
  }

  clearFilters() {
    this.searchName = '';
    this.filterDeptId = null;
    this.filterMinSalary = null;
    this.filteredList = [...this.staffList];
  }

  // ======= ADD =======
  openAddModal() {
    this.isEditMode = false;
    this.editingId = null;
    this.modalError = '';
    this.formData = { staffName: '', departmentId: 0, salary: 0 };
    this.showModal = true;
  }

  // ======= EDIT =======
  openEditModal(staff: FacultyRes) {
    this.isEditMode = true;
    this.editingId = staff.staffId;
    this.modalError = '';
    this.formData = {
      staffName: staff.staffName,
      departmentId: staff.departmentId,
      salary: staff.salary
    };
    this.showModal = true;
  }

  // ======= SAVE =======
  saveStaff() {
    if (!this.formData.staffName || !this.formData.departmentId || !this.formData.salary) {
      this.modalError = 'All fields are required';
      return;
    }

    if (this.isEditMode && this.editingId !== null) {
      const index = this.staffList.findIndex(s => s.staffId === this.editingId);
      if (index !== -1) {
        this.staffList[index] = { staffId: this.editingId, ...this.formData };
      }
      this.successMsg = 'Staff updated successfully!';
    } else {
      const newId = this.staffList.length > 0
        ? Math.max(...this.staffList.map(s => s.staffId)) + 1
        : 1;
      this.staffList.push({ staffId: newId, ...this.formData });
      this.successMsg = 'Staff added successfully!';
    }

    this.filteredList = [...this.staffList];
    this.saveToStorage();
    this.showModal = false;
    setTimeout(() => this.successMsg = '', 3000);
  }

  // ======= DELETE =======
  openDeleteModal(staff: FacultyRes) {
    this.deletingId = staff.staffId;
    this.deletingName = staff.staffName;
    this.showDeleteModal = true;
  }

  confirmDelete() {
    this.staffList = this.staffList.filter(s => s.staffId !== this.deletingId);
    this.filteredList = [...this.staffList];
    this.saveToStorage();
    this.successMsg = 'Staff deleted successfully!';
    this.showDeleteModal = false;
    setTimeout(() => this.successMsg = '', 3000);
  }
}
