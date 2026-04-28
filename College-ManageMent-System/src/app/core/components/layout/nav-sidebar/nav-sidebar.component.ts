import { Component } from '@angular/core';

@Component({
  selector: 'app-nav-sidebar',
  standalone: false,
  templateUrl: './nav-sidebar.component.html',
  styleUrl: './nav-sidebar.component.css'
})
export class NavSidebarComponent {
  navItems = [
    { path: '/view/dashboard', label: 'Dashboard' },
    { path: '/view/attendance', label: 'Attendance' },
    { path: '/view/book', label: 'Books' },
    { path: '/view/book-issue', label: 'Book Issues' },
    { path: '/view/canteen', label: 'Canteen' },
    { path: '/view/classroom', label: 'Classrooms' },
    { path: '/view/course', label: 'Courses' },
    { path: '/view/department', label: 'Departments' },
    { path: '/view/events', label: 'Events' },
    { path: '/view/exam', label: 'Exams' },
    { path: '/view/faculty', label: 'Faculty' },
    { path: '/view/fees', label: 'Fees' },
    { path: '/view/hostel', label: 'Hostel' },
    { path: '/view/infrastructure', label: 'Infrastructure' },
    { path: '/view/library', label: 'Library' },
    { path: '/view/library-member', label: 'Library Members' },
    { path: '/view/pg-student', label: 'PG Students' },
    { path: '/view/results', label: 'Results' },
    { path: '/view/students', label: 'Students' },
    { path: '/view/subject', label: 'Subjects' },
    { path: '/view/ug-program', label: 'UG Programs' }
  ];
}
