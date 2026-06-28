import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { LoginDTO } from '../DTOs/LoginDTO';
import { SessionService } from '../services/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;

  constructor(
    private router: Router,
    private userService: UserService,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    });
  }

  onLogin() {
    if (this.loginForm.invalid) {
      alert('Fill all fields correctly');
      return;
    }

    const form = this.loginForm.value;

    const loginDTO: LoginDTO = {
      email: form.email,
      password: form.password
    };

    this.userService.login(loginDTO).subscribe({
      next: (res) => {
        console.log('Login success:', res);

        this.session.setUser(res);

        this.router.navigate(['/garden']);
      },
      error: (err) => {
        console.log(err);
        alert(err?.error?.message || 'Login failed');
      }
    });
  }
}