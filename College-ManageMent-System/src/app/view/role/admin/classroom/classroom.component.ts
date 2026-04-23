import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClassroomService } from '../../../services/classroom/classroom.service';
import { DepartmentService } from '../../../services/department/department.service';
import { ClassroomReq } from '../../../models/request_dto/classroom-req';
import { ClassroomRes } from '../../../models/response_dto/classroom-res';
import { DepartmentResponse } from '../../../models/response_dto/department';

@Component({
  selector: 'app-classroom',
  standalone: false,
  templateUrl: './classroom.component.html',
  styleUrls: ['./classroom.component.css']
})
export class ClassroomComponent implements OnInit {
  classroomList: ClassroomRes[] = [];
  departments: DepartmentResponse[] = [];
  
  showForm: boolean = false;
  classroomForm!: FormGroup;
  editingId: number | null = null;
  
  typeOptions = ['LECTURE_HALL', 'LABORATORY', 'SEMINAR_ROOM', 'COMPUTER_LAB', 'WORKSHOP', 'AUDITORIUM'];

  filterType: string = '';

  constructor(
    private fb: FormBuilder,
    private classroomService: ClassroomService,
    private departmentService: DepartmentService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadClassrooms();
    this.loadDepartments();
  }

  initForm(): void {
    this.classroomForm = this.fb.group({
      roomNumber: ['', Validators.required],
      building: ['', Validators.required],
      floor: [null],
      capacity: [null, [Validators.required, Validators.min(1)]],
      classroomType: ['LECTURE_HALL', Validators.required],
      hasProjector: [false],
      hasAC: [false],
      isAvailable: [true],
      departmentId: [null, Validators.required]
    });
  }

  loadClassrooms(): void {
    this.classroomService.getClassrooms().subscribe({
      next: (data) => {
        this.classroomList = data;
        this.applyFilters();
      },
      error: (err) => console.error('Failed to load classrooms', err)
    });
  }

  loadDepartments(): void {
    this.departmentService.getDepts().subscribe({
      next: (data) => this.departments = data,
      error: (err) => console.error('Failed to load departments', err)
    });
  }

  applyFilters(): void {
    if (this.filterType) {
      this.classroomService.getByType(this.filterType).subscribe(res => {
         this.classroomList = res;
      });
    }
  }

  onFilterTypeChange(type: string): void {
    this.filterType = type;
    if(type) {
       this.classroomService.getByType(type).subscribe(data => this.classroomList = data);
    } else {
       this.loadClassrooms();
    }
  }

  getAvailableOnly(): void {
    this.filterType = '';
    this.classroomService.getAvailable().subscribe({
      next: (data) => this.classroomList = data,
      error: (err) => console.error('Failed to load available classrooms', err)
    });
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
    if (!this.showForm) {
      this.resetForm();
    }
  }

  closeForm(): void {
    this.showForm = false;
    this.resetForm();
  }

  resetForm(): void {
    this.editingId = null;
    this.classroomForm.reset();
    this.classroomForm.patchValue({ classroomType: 'LECTURE_HALL', hasProjector: false, hasAC: false, isAvailable: true });
  }

  onSubmit(): void {
    if (this.classroomForm.invalid) {
      return;
    }

    const payload: ClassroomReq = this.classroomForm.value;

    if (this.editingId) {
      this.classroomService.updateClassroom(this.editingId, payload).subscribe({
        next: () => {
          this.loadClassrooms();
          this.closeForm();
        },
        error: (err) => console.error('Failed to update', err)
      });
    } else {
      this.classroomService.addClassroom(payload).subscribe({
        next: () => {
          this.loadClassrooms();
          this.closeForm();
        },
        error: (err) => console.error('Failed to create', err)
      });
    }
  }

  editClassroom(item: ClassroomRes): void {
    this.editingId = item.id;
    const dept = this.departments.find(d => d.name === item.departmentName);

    this.classroomForm.patchValue({
      roomNumber: item.roomNumber,
      building: item.building,
      floor: item.floor,
      capacity: item.capacity,
      classroomType: item.classroomType,
      hasProjector: item.hasProjector,
      hasAC: item.hasAC,
      isAvailable: item.isAvailable,
      departmentId: dept ? dept.id : null
    });
    this.showForm = true;
  }

  deleteClassroom(id: number): void {
    if (confirm('Are you sure you want to delete this classroom?')) {
      this.classroomService.deleteClassroom(id).subscribe({
        next: () => this.loadClassrooms(),
        error: (err) => console.error('Failed to delete', err)
      });
    }
  }

  toggleAvailability(item: ClassroomRes): void {
    const newVal = !item.isAvailable;
    this.classroomService.toggleAvailability(item.id, newVal).subscribe({
       next: (updated) => {
          item.isAvailable = updated.isAvailable;
       },
       error: (err) => console.error('Failed to patch availability', err)
    });
  }
}
