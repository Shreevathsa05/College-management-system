import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-faculty-navbar',
  templateUrl: './faculty-navbar.component.html',
  styleUrl: './faculty-navbar.component.css'
})
export class FacultyNavbarComponent {

  constructor(private router: Router) {}

  // ✅ NOW EXISTS — was missing before, causing *ngIf to hide the whole navbar
  isFacultyRoute(): boolean {
    return this.router.url.includes('/view/faculty');
  }

  logout(): void {
    this.router.navigate(['/login']);
  }

}
