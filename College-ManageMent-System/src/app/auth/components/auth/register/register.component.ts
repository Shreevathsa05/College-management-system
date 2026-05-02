import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { passwordMatchValidator } from '../utils/passwordValidators';
import { AuthService } from '../../../service/auth.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;
  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]],
      role: ['ADMIN', Validators.required]
    }, { validators: passwordMatchValidator });
  }
  registerSubmit() {
    if (this.registerForm.valid) {
      const payload = {
        username: this.registerForm.value.username,
        password: this.registerForm.value.password,
        role: this.registerForm.value.role,
        linkedId: 1
      };

      console.log('Success payload: ', payload);
      this.authService.register(payload).subscribe({
        next: (response) => {
          console.log("Success");
          localStorage.setItem('token', response.token);
          // this.router.navigate(['/login']);
        },
        error: (error) => {
          console.log("Error" + JSON.stringify(error));
        }
      });
    } else {
      console.log(this.registerForm.errors);
      this.printErrors();
    }
  }
  printErrors() {
    for (const controlName in this.registerForm.controls) {
      const control = this.registerForm.get(controlName);
      if (control && control.errors) {
        console.log(`Errors in ${controlName}:`, control.errors);
      }
    }
  }
}