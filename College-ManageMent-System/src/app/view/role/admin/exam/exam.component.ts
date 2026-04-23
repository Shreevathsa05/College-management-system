import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ExamService } from '../../../services/exam/exam.service';
import { SubjectService } from '../../../services/subject/subject.service';
import { ClassroomService } from '../../../services/classroom/classroom.service';
import { ExamReq } from '../../../models/request_dto/exam-req';
import { ExamRes } from '../../../models/response_dto/exam-res';
import { SubjectRes } from '../../../models/response_dto/subject-res';
import { ClassroomRes } from '../../../models/response_dto/classroom-res';

@Component({
  selector: 'app-exam',
  standalone: false,
  templateUrl: './exam.component.html',
  styleUrls: ['./exam.component.css']
})
export class ExamComponent implements OnInit {
  examsList: ExamRes[] = [];
  subjects: SubjectRes[] = [];
  classrooms: ClassroomRes[] = [];

  showForm: boolean = false;
  examForm!: FormGroup;
  editingId: number | null = null;
  
  examTypes = ['MID_TERM', 'FINAL', 'UNIT_TEST', 'PRACTICAL', 'VIVA'];
  
  filterType: string = '';
  filterSession: string = '';

  constructor(
    private fb: FormBuilder,
    private examService: ExamService,
    private subjectService: SubjectService,
    private classroomService: ClassroomService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadExams();
    this.loadSubjects();
    this.loadClassrooms();
  }

  initForm(): void {
    this.examForm = this.fb.group({
      examType: ['MID_TERM', Validators.required],
      session: ['', Validators.required],
      examDate: ['', Validators.required],
      startTime: [''],
      endTime: [''],
      maxMarks: [null, [Validators.required, Validators.min(1)]],
      passingMarks: [null],
      subjectId: [null, Validators.required],
      classroomId: [null]
    });
  }

  loadExams(): void {
    this.examService.getExams().subscribe({
      next: (data) => {
        this.examsList = data;
        this.applyFilters();
      },
      error: (err) => console.error('Failed to load exams', err)
    });
  }

  loadSubjects(): void {
    this.subjectService.getSubjects().subscribe({
      next: (data) => this.subjects = data,
      error: (err) => console.error('Failed to load subjects', err)
    });
  }

  loadClassrooms(): void {
    this.classroomService.getClassrooms().subscribe({
      next: (data) => this.classrooms = data,
      error: (err) => console.error('Failed to load classrooms', err)
    });
  }

  applyFilters(): void {
    if (this.filterSession) {
      this.examService.getBySession(this.filterSession).subscribe(res => this.examsList = res);
    } else if (this.filterType) {
      this.examService.getByType(this.filterType).subscribe(res => this.examsList = res);
    }
  }

  onFilterTypeChange(type: string): void {
    this.filterType = type;
    this.filterSession = '';
    if (type) {
      this.examService.getByType(type).subscribe(data => this.examsList = data);
    } else {
      this.loadExams();
    }
  }

  onFilterSessionChange(session: string): void {
    this.filterSession = session;
    this.filterType = '';
    if (session) {
      this.examService.getBySession(session).subscribe(data => this.examsList = data);
    } else {
      this.loadExams();
    }
  }

  getUpcomingExams(): void {
    this.filterType = '';
    this.filterSession = '';
    this.examService.getUpcoming().subscribe(data => this.examsList = data);
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
    this.examForm.reset();
    this.examForm.patchValue({ examType: 'MID_TERM' });
  }

  onSubmit(): void {
    if (this.examForm.invalid) {
      return;
    }

    const payload: ExamReq = this.examForm.value;

    if (this.editingId) {
      this.examService.updateExam(this.editingId, payload).subscribe({
        next: () => {
          this.loadExams();
          this.closeForm();
        },
        error: (err) => console.error('Failed to update', err)
      });
    } else {
      this.examService.addExam(payload).subscribe({
        next: () => {
          this.loadExams();
          this.closeForm();
        },
        error: (err) => console.error('Failed to create', err)
      });
    }
  }

  editExam(item: ExamRes): void {
    this.editingId = item.id;
    this.examForm.patchValue({
      examType: item.examType,
      session: item.session,
      examDate: item.examDate,
      startTime: item.startTime,
      endTime: item.endTime,
      maxMarks: item.maxMarks,
      passingMarks: item.passingMarks,
      subjectId: item.subjectId,
      classroomId: item.classroomId
    });
    this.showForm = true;
  }

  deleteExam(id: number): void {
    if (confirm('Are you sure you want to delete this exam?')) {
      this.examService.deleteExam(id).subscribe({
        next: () => this.loadExams(),
        error: (err) => console.error('Failed to delete', err)
      });
    }
  }
}
