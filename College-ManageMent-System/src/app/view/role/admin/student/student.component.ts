import { Component, OnInit } from '@angular/core';
import { StudentService } from '../../../services/student/student.service';
import { StudentRes } from '../../../models/response_dto/student-res';
import { StudentReq } from '../../../models/request_dto/student-req';
import { ToastrService } from 'ngx-toastr';
import { DepartmentService } from '../../../services/department/department.service';
import { CourseService } from '../../../services/course/course.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrl: './student.component.css',
  standalone: false
})
export class StudentComponent implements OnInit {

  students: StudentRes[] = [];
  filteredStudents: StudentRes[] = [];
  departments: any[] = [];
  courses: any[] = [];
  genders = ['MALE', 'FEMALE', 'OTHER'];

  statuses = ['ACTIVE', 'INACTIVE', 'GRADUATED', 'DROPPED'];

  admissionTypes = ['REGULAR', 'LATERAL', 'MANAGEMENT', 'NRI', 'SCHOLARSHIP'];

  bloodGroups = [
    'A_POSITIVE', 'A_NEGATIVE',
    'B_POSITIVE', 'B_NEGATIVE',
    'O_POSITIVE', 'O_NEGATIVE',
    'AB_POSITIVE', 'AB_NEGATIVE'
  ];

  emailPattern = /^[^@\s]+@[^@\s]+\.com$/;
  phonePattern = /^[0-9]{10}$/;
  todayDate = new Date().toISOString().slice(0, 10);

  // Summary cards
  totalStudents = 0;
  activeStudents = 0;
  graduatedStudents = 0;
  droppedStudents = 0;

  // Filters
  selectedDepartment: number | null = null;
  selectedCourse: number | null = null;
  selectedStatus: string | null = null;
  selectedSemester: number | null = null;
  selectedPassoutYear: number | null = null;

  formData: StudentReq = this.getEmptyForm();

  isEditMode = false;
  selectedId: number | null = null;
  showFormModal = false;
  searchQuery = '';

  constructor(private studentService: StudentService, private toastr: ToastrService, private departmentService: DepartmentService, private courseService: CourseService) {}

  ngOnInit(): void {
    this.loadStudents();
    this.loadDepartments();
    this.loadCourses();
  }

  // EMPTY FORM TEMPLATE
  getEmptyForm(): StudentReq {
    return {
      firstName: '',
      lastName: '',
      gender: 'MALE',
      dob: '',
      rollNo: '',
      email: '',
      phone: '',
      currentSemester: 1,
      admissionYear: new Date().getFullYear(),
      status: 'ACTIVE',
      address: '',
      enrollmentNumber: '',
      admissionType: null,
      bloodGroup: null,
      profileImageUrl: '',
      departmentId: null as unknown as number, 
      courseId: null as unknown as number      
    };
  }

  // LOAD ALL
  loadStudents() {
    this.studentService.getStudents().subscribe({
      next: (res) => {
        this.students = res;
        this.filteredStudents = res;
        this.calculateSummaries(res);
      },
      error: (err) => {
        console.error("Error fetching students", err);
        this.toastr.error("Failed to load students", "Error");
      }
    });
  }

  // Calculate summary counts
  calculateSummaries(items: StudentRes[] = this.filteredStudents) {
    this.totalStudents = items.length;
    this.activeStudents = items.filter(s => s.status === 'ACTIVE').length;
    this.graduatedStudents = items.filter(s => s.status === 'GRADUATED').length;
    this.droppedStudents = items.filter(s => s.status === 'DROPPED').length;
  }

  // Search handler
  onSearchChange() {
    const query = this.searchQuery?.trim() ?? '';
    if (query.length > 0) {
      this.studentService.searchStudents(query).subscribe({
        next: (res) => {
          this.filteredStudents = res;
          this.calculateSummaries(res);
        },
        error: (err) => {
          console.error("Error searching students", err);
          this.toastr.error("Failed to search students", "Error");
        }
      });
    } else {
      this.applyFilters();
    }
  }

  applyFilters() {
    this.studentService.filterStudents(
      this.selectedDepartment,
      this.selectedCourse,
      this.selectedStatus,
      this.selectedSemester,
      this.selectedPassoutYear
    ).subscribe({
      next: (res) => {
        this.filteredStudents = res;
        this.calculateSummaries(res);
      },
      error: (err) => {
        console.error("Error filtering students", err);
        this.toastr.error("Failed to filter students", "Error");
      }
    });
  }

  onFilterChange() {
    this.searchQuery = '';
    this.applyFilters();
  }

  // CREATE / UPDATE
  submitForm() {
    if (!this.formData.bloodGroup) this.formData.bloodGroup = null;
    if (!this.formData.admissionType) this.formData.admissionType = null;

    // ENROLLMENT FIX (ADD HERE)
    if (!this.formData.enrollmentNumber) {
      this.formData.enrollmentNumber = null;
    }

    // VALIDATION
    if (!this.formData.firstName ||
        !this.formData.rollNo ||
        !this.formData.departmentId ||
        !this.formData.courseId) {
      this.toastr.error("Please fill all required fields", "Validation Error");
      return;
    }

    if (this.formData.currentSemester < 1 || this.formData.currentSemester > 8) {
      this.toastr.error("Semester must be between 1 and 8", "Validation Error");
      return;
    }

    if (this.formData.email && !this.emailPattern.test(this.formData.email)) {
      this.toastr.error("Email must include @ and end with .com", "Validation Error");
      return;
    }

    if (this.formData.phone && !this.phonePattern.test(this.formData.phone)) {
      this.toastr.error("Phone number must be exactly 10 digits", "Validation Error");
      return;
    }

    if (this.isEditMode && this.selectedId) {
      this.studentService.updateStudent(this.selectedId, this.formData).subscribe({
        next: () => {
          this.toastr.success("Student updated successfully", "Success");
          //this.resetForm();
          this.closeFormModal();
          this.loadStudents();
        },
        error: (err) => this.toastr.error(err.error?.message || "Update failed", "Error")
      });
    } else {
      this.studentService.addStudent(this.formData).subscribe({
        next: () => {
          this.toastr.success("Student created successfully", "Success");
          //this.resetForm();
          this.closeFormModal();
          this.loadStudents();
        },
        error: (err) => this.toastr.error(err.error?.message || "Create failed", "Error")
      });
    }
  }

  // EDIT (FIXED PROPER MAPPING)
  editStudent(student: StudentRes) {
    this.formData = {
      firstName: student.firstName,
      lastName: student.lastName || '',
      gender: student.gender as any,
      dob: student.dob || '',
      rollNo: student.rollNo,
      email: student.email || '',
      phone: student.phone || '',
      currentSemester: student.currentSemester,
      admissionYear: student.admissionYear,
      status: student.status as any,
      address: student.address || '',
      enrollmentNumber: student.enrollmentNumber || '',
      admissionType: student.admissionType || null,
      bloodGroup: student.bloodGroup || null,
      profileImageUrl: student.profileImageUrl || '',
      departmentId: this.departments.find(d => d.name === student.departmentName)?.id ?? null,
      courseId: this.courses.find(c => c.courseTitle === student.courseName)?.id ?? null

    };

    this.selectedId = student.id;
    this.isEditMode = true;
  }

  // DELETE
  deleteStudent(id: number) {
    if (confirm("Are you sure you want to delete this student?")) {
      this.studentService.deleteStudent(id).subscribe(() => {
        this.toastr.success("Student deleted successfully", "Success");
        this.loadStudents();
      }, (err) => {
        this.toastr.error("Failed to delete student", "Error");
      });
    }
  }

  openFormModal() {
    this.resetForm();
    this.showFormModal = true;
  }

  closeFormModal() {
    this.showFormModal = false;
    this.resetForm();
  }

  openEditFormModal(student: StudentRes) {
    this.editStudent(student);
    this.showFormModal = true;
  }

  // RESET
  resetForm() {
    this.formData = this.getEmptyForm();
    this.isEditMode = false;
    this.selectedId = null;
  }

  loadDepartments() {
  this.departmentService.getDepts().subscribe({
    next: (res) => this.departments = res,
    error: (err) => console.error("Error loading departments", err)
  });
}

loadCourses() {
  this.courseService.getCourses().subscribe({
    next: (res) => this.courses = res,
    error: (err) => console.error("Error loading courses", err)
  });
}


}