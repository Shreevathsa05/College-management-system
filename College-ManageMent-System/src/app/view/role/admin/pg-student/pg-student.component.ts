import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PgStudentService } from '../../../services/pg-student/pg-student.service'; // Adjust path if needed
import { PgStudentRes } from '../../../models/response_dto/pg-student-res'; // Adjust path if needed

@Component({
  selector: 'app-pg-student',
  templateUrl: './pg-student.component.html',
  styleUrls: ['./pg-student.component.css'],
  standalone: false,
})
export class PgStudentComponent implements OnInit {
  pgStudents: PgStudentRes[] = [];
  pgStudentForm!: FormGroup;
  showForm = false;
  editingId: number | string | null = null;

  constructor(
    private fb: FormBuilder,
    private pgStudentService: PgStudentService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadPgStudents();
  }

  initForm(): void {
    // Maps to the PgStudentReq fields specified in your API documentation
    this.pgStudentForm = this.fb.group({
      studentId: [null, Validators.required],
      researchTopic: [''],
      supervisorName: [''],
      thesisTitle: [''],
      pgStartDate: [''], 
      expectedCompletionDate: [''],
      programType: [''],
      isThesisSubmitted: [false]
    });
  }

  loadPgStudents(): void {
    this.pgStudentService.getPgStudents().subscribe({
      next: (res) => {
        this.pgStudents = res;
      },
      error: (err) => console.error('Error fetching PG students', err),
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
    this.pgStudentForm.reset({ isThesisSubmitted: false }); 
  }

  editPgStudent(student: PgStudentRes): void {
    this.editingId = student.id;
    
    // We patch the form with the student data.
    // Note: HTML <input type="date"> expects dates in YYYY-MM-DD format.
    // Based on your Swagger example ("2026-04-26"), it should patch perfectly.
    this.pgStudentForm.patchValue({
      studentId: student.id, // Or whichever field maps to the initial student ID creation
      researchTopic: student.researchTopic,
      supervisorName: student.supervisorName,
      thesisTitle: student.thesisTitle,
      pgStartDate: student.pgStartDate,
      expectedCompletionDate: student.expectedCompletionDate,
      programType: student.programType,
      isThesisSubmitted: student.isThesisSubmitted
    });

    this.showForm = true;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  deletePgStudent(id: number | string): void {
    if (confirm('Are you sure you want to delete this PG student record?')) {
      this.pgStudentService.deletePgStudent(id).subscribe({
        next: () => this.loadPgStudents(),
        error: (err) => console.error('Error deleting PG student', err),
      });
    }
  }

  onSubmit(): void {
    if (this.pgStudentForm.invalid) {
      this.pgStudentForm.markAllAsTouched();
      return;
    }

    const formData = this.pgStudentForm.value;

    if (this.editingId) {
      // API expects PUT to have studentId in the body
      this.pgStudentService.updatePgStudent(this.editingId, formData).subscribe({
        next: () => {
          this.loadPgStudents();
          this.closeForm();
        },
        error: (err) => console.error('Error updating PG student', err),
      });
    } else {
      this.pgStudentService.addPgStudent(formData).subscribe({
        next: () => {
          this.loadPgStudents();
          this.closeForm();
        },
        error: (err) => console.error('Error adding PG student', err),
      });
    }
  }
}