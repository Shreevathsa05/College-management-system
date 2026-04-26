import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FacultyService } from '../../../services/faculty/faculty.service'; // Adjust path if needed
import { FacultyRes } from '../../../models/response_dto/faculty-res'; // Adjust path if needed

@Component({
  selector: 'app-faculty',
  templateUrl: './faculty.component.html',
  styleUrls: ['./faculty.component.css'],
  standalone: false,
})
export class FacultyComponent implements OnInit {
  facultyList: FacultyRes[] = [];
  facultyForm!: FormGroup;
  showForm = false;
  editingId: number | string | null = null;

  constructor(
    private fb: FormBuilder,
    private facultyService: FacultyService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadFaculty();
  }

  initForm(): void {
    // Maps to the FacultyReq fields specified in your API documentation
    this.facultyForm = this.fb.group({
      fullName: ['', Validators.required],
      employeeCode: ['', Validators.required],
      email: [''],
      phone: [''],
      designation: [''],
      qualification: [''],
      specialization: [''],
      departmentId: [null, Validators.required], 
      cabinNo: [''],
      dateOfJoining: [''], 
      salary: [null]
    });
  }

  loadFaculty(): void {
    this.facultyService.getFacultyList().subscribe({
      next: (res) => {
        this.facultyList = res;
      },
      error: (err) => console.error('Error fetching faculty data', err),
    });
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
    if (!this.showForm) {
      this.closeForm();
    }
  }

  closeForm(): void {
    this.showForm = false;
    this.editingId = null;
    this.facultyForm.reset(); 
  }

  editFaculty(faculty: FacultyRes): void {
    this.editingId = faculty.id; 
    
    this.facultyForm.patchValue({
      fullName: faculty.fullName,
      employeeCode: faculty.employeeCode,
      email: faculty.email,
      phone: faculty.phone,
      designation: faculty.designation,
      qualification: faculty.qualification,
      specialization: faculty.specialization,
      cabinNo: faculty.cabinNo,
      dateOfJoining: faculty.dateOfJoining,
      salary: faculty.salary
      // Note: Your GET response returns 'departmentName' but your PUT/POST request 
      // requires 'departmentId'. When editing, the user will need to re-enter 
      // the department ID unless you fetch and map it separately.
    });

    this.showForm = true;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  deleteFaculty(id: number | string): void {
    if (confirm('Are you sure you want to delete this faculty record?')) {
      this.facultyService.deleteFaculty(id).subscribe({
        next: () => this.loadFaculty(),
        error: (err) => console.error('Error deleting faculty record', err),
      });
    }
  }

  onSubmit(): void {
    if (this.facultyForm.invalid) {
      this.facultyForm.markAllAsTouched();
      return;
    }

    const formData = this.facultyForm.value;

    if (this.editingId) {
      this.facultyService.updateFaculty(this.editingId, formData).subscribe({
        next: () => {
          this.loadFaculty();
          this.closeForm();
        },
        error: (err) => console.error('Error updating faculty record', err),
      });
    } else {
      this.facultyService.addFaculty(formData).subscribe({
        next: () => {
          this.loadFaculty();
          this.closeForm();
        },
        error: (err) => console.error('Error adding faculty record', err),
      });
    }
  }
}