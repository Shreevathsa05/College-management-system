import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  applicationName = "College Management System";

  constructor(private router: Router) { }

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }


  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/']);
  }
}

