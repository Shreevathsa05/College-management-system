import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { ViewRoutingModule } from './view-routing.module';
import { DepartmentComponent } from './role/admin/department/department.component';
import { AttendanceComponent } from './role/admin/attendance/attendance.component';
import { BookComponent } from './role/admin/book/book.component';
import { BookIssueComponent } from './role/admin/book-issue/book-issue.component';
import { CanteenComponent } from './role/admin/canteen/canteen.component';
import { ClassroomComponent } from './role/admin/classroom/classroom.component';
import { CourseComponent } from './role/admin/course/course.component';
import { DashboardComponent } from './role/admin/dashboard/dashboard.component';
import { EventsComponent } from './role/admin/events/events.component';
import { ExamComponent } from './role/admin/exam/exam.component';
import { FacultyComponent } from './role/admin/faculty/faculty.component';
import { FeesComponent } from './role/admin/fees/fees.component';
import { HostelComponent } from './role/admin/hostel/hostel.component';
import { InfrastructureComponent } from './role/admin/infrastructure/infrastructure.component';
import { LibraryComponent } from './role/admin/library/library.component';
import { LibraryMemberComponent } from './role/admin/library-member/library-member.component';
import { PgStudentComponent } from './role/admin/pg-student/pg-student.component';
import { ResultsComponent } from './role/admin/results/results.component';
import { StudentComponent } from './role/admin/student/student.component';
import { SubjectComponent } from './role/admin/subject/subject.component';
import { UgprogramComponent } from './role/admin/ugprogram/ugprogram.component';

@NgModule({
  declarations: [
    DepartmentComponent,
    AttendanceComponent,
    BookComponent,
    BookIssueComponent,
    CanteenComponent,
    ClassroomComponent,
    CourseComponent,
    DashboardComponent,
    EventsComponent,
    ExamComponent,
    FacultyComponent,
    FeesComponent,
    HostelComponent,
    InfrastructureComponent,
    LibraryComponent,
    LibraryMemberComponent,
    PgStudentComponent,
    ResultsComponent,
    StudentComponent,
    SubjectComponent,
    UgprogramComponent
  ],
  imports: [
    CommonModule,
    ViewRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class ViewModule { }
