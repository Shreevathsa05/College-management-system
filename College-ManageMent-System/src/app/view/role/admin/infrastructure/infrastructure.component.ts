import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InfrastructureService } from '../../../services/infrastructure/infrastructure.service';
import { DepartmentService } from '../../../services/department/department.service';
import { InfrastructureReq } from '../../../models/request_dto/infrastructure-req';
import { InfrastructureRes } from '../../../models/response_dto/infrastructure-res';
import { DepartmentResponse } from '../../../models/response_dto/department';

@Component({
  selector: 'app-infrastructure',
  standalone: false,
  templateUrl: './infrastructure.component.html',
  styleUrls: ['./infrastructure.component.css']
})
export class InfrastructureComponent implements OnInit {
  infrastructureList: InfrastructureRes[] = [];
  departments: DepartmentResponse[] = [];
  
  showForm: boolean = false;
  infrastructureForm!: FormGroup;
  editingId: number | null = null;
  
  statusOptions = ['AVAILABLE', 'UNDER_MAINTENANCE', 'DECOMMISSIONED'];
  typeOptions = ['CLASSROOM', 'LABORATORY', 'COMPUTER_LAB', 'SERVER_ROOM', 'OFFICE', 'WORKSHOP', 'COMMON_AREA'];

  filterType: string = '';
  filterStatus: string = '';

  constructor(
    private fb: FormBuilder,
    private infrastructureService: InfrastructureService,
    private departmentService: DepartmentService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadInfrastructure();
    this.loadDepartments();
  }

  initForm(): void {
    this.infrastructureForm = this.fb.group({
      roomOrLabName: ['', Validators.required],
      floor: [null],
      block: ['', Validators.required],
      capacity: [null, [Validators.required, Validators.min(1)]],
      hasProjector: [false],
      noOfComputers: [null],
      status: ['AVAILABLE', Validators.required],
      type: ['CLASSROOM', Validators.required],
      departmentId: [null, Validators.required]
    });
  }

  loadInfrastructure(): void {
    this.infrastructureService.getAll().subscribe({
      next: (data) => {
        this.infrastructureList = data;
        this.applyFilters();
      },
      error: (err) => console.error('Failed to load infrastructure', err)
    });
  }

  loadDepartments(): void {
    this.departmentService.getDepts().subscribe({
      next: (data) => this.departments = data,
      error: (err) => console.error('Failed to load departments', err)
    });
  }

  applyFilters(): void {
    let finalSource = this.infrastructureList;
    if (this.filterStatus) {
      this.infrastructureService.getByStatus(this.filterStatus).subscribe(res => {
         this.infrastructureList = res;
      });
    } else if (this.filterType) {
      this.infrastructureService.getByType(this.filterType).subscribe(res => {
         this.infrastructureList = res;
      });
    } else {
      // Just showing list in loadInfrastructure already
    }
  }

  onFilterStatusChange(status: string): void {
    this.filterStatus = status;
    this.filterType = ''; // clear other filter
    if(status) {
       this.infrastructureService.getByStatus(status).subscribe(data => this.infrastructureList = data);
    } else {
       this.loadInfrastructure();
    }
  }

  onFilterTypeChange(type: string): void {
    this.filterType = type;
    this.filterStatus = ''; // clear other filter
    if(type) {
       this.infrastructureService.getByType(type).subscribe(data => this.infrastructureList = data);
    } else {
       this.loadInfrastructure();
    }
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
    this.infrastructureForm.reset();
    this.infrastructureForm.patchValue({ status: 'AVAILABLE', type: 'CLASSROOM' });
  }

  onSubmit(): void {
    if (this.infrastructureForm.invalid) {
      return;
    }

    const payload: InfrastructureReq = this.infrastructureForm.value;

    if (this.editingId) {
      this.infrastructureService.update(this.editingId, payload).subscribe({
        next: () => {
          this.loadInfrastructure();
          this.closeForm();
        },
        error: (err) => console.error('Failed to update', err)
      });
    } else {
      this.infrastructureService.addInfrastructure(payload).subscribe({
        next: () => {
          this.loadInfrastructure();
          this.closeForm();
        },
        error: (err) => console.error('Failed to create', err)
      });
    }
  }

  editInfrastructure(item: InfrastructureRes): void {
    this.editingId = item.id;
    // We need standard departmentId form matching, but infrastructureRes does not provide departmentId, only name.
    // So we try to find the department ID by matching the name.
    const dept = this.departments.find(d => d.name === item.departmentName);

    this.infrastructureForm.patchValue({
      roomOrLabName: item.roomOrLabName,
      floor: item.floor,
      block: item.block,
      capacity: item.capacity,
      hasProjector: item.hasProjector,
      noOfComputers: item.noOfComputers,
      status: item.status,
      type: item.type,
      departmentId: dept ? dept.id : null
    });
    this.showForm = true;
  }

  deleteInfrastructure(id: number): void {
    if (confirm('Are you sure you want to delete this infrastructure?')) {
      this.infrastructureService.delete(id).subscribe({
        next: () => this.loadInfrastructure(),
        error: (err) => console.error('Failed to delete', err)
      });
    }
  }

  updateStatus(id: number, status: string): void {
    this.infrastructureService.updateStatus(id, status).subscribe({
       next: () => this.loadInfrastructure(),
       error: (err) => console.error('Failed to update status', err)
    });
  }
}
