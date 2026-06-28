import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { CreateUserDTO } from '../DTOs/CreateUserDTO';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registerForm!: FormGroup;

  constructor(
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.registerForm = new FormGroup(
      {
        name: new FormControl('', Validators.required),
        surname: new FormControl('', Validators.required),
        email: new FormControl('', [Validators.required, Validators.email]),
        password_first: new FormControl('', Validators.required),
        password_second: new FormControl('', Validators.required),
      },
      { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(control: AbstractControl) {
    const pass = control.get('password_first')?.value;
    const repeat = control.get('password_second')?.value;

    if (!pass || !repeat) {
      return null;
    }

    return pass === repeat ? null : { passwordMismatch: true };
  }

  onRegister() {
    if (this.registerForm.invalid) {
      alert('Form is invalid');
      return;
    }

    if (this.registerForm.errors?.['passwordMismatch']) {
      alert('Passwords do not match');
      return;
    }

    const form = this.registerForm.value;

    const newUser: CreateUserDTO = {
      name: form.name,
      surname: form.surname,
      email: form.email,
      password: form.password_first
    };

    this.userService.register(newUser).subscribe({
      next: () => {
        alert('Registration successful');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.log(err);
        alert(err?.error?.message || 'Registration failed');
      }
    });
  }
}