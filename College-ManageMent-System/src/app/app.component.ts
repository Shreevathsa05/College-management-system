import { Component } from '@angular/core';
import { Router } from '@angular/router'; // ✅ ADD THIS

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrls: ['./app.component.css'] // ⚠️ small fix: stylesUrl → stylesUrls
})
export class AppComponent {
  title = 'College-ManageMent-System';

  constructor(private router: Router) {} // ✅ ADD THIS


    isFacultyRoute(): boolean {
      return this.router.url.includes('/view/faculty');
    }
  }

