import { Component, OnInit } from '@angular/core';
import { NgForm, FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { createUserDTO, UserDTO } from '../DTOs/UserDTO';
// import { AuthenticationDetails, CognitoUser, CognitoUserPool } from 'amazon-cognito-identity-js';
// import { environment } from 'src/environments/environment';
// import { JwtService } from '../jwt.service';
// import { HelloService } from '../backend_services/hello.service';
// import { CognitoService } from '../backend_services/cognito.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  isLoading: boolean = false;
  username: string = "";
  password: string = "";
  registerForm!: FormGroup;
  isDisabled: boolean = false;

  constructor(private router: Router, private UserService: UserService) { 
  }

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      btn: new FormControl("")},
      // { validators: this.check },
    );
  }

  onRegister() {
    this.username = this.registerForm.get('username')?.value;
    this.password = this.registerForm.get('password')?.value
    let newUser = createUserDTO(this.username, this.password);
    // console.log(this.username);
    // console.log(this.password);
    // this.router.navigate(['trending']) //.then(()=>{location.reload();});
    this.UserService.register(newUser).subscribe({
      next: result => {
        alert(result.message);
        // console.log(result.message);
      },
      error: err => {
        console.log(err);
        alert(err?.error?.message || JSON.stringify(err));
      }

    })
  }

  check(control: AbstractControl) {
    const usernameRegex = /^[a-zA-Z0-9]+$/;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+{};:,<.>])(?=.*[^\s]).{8,}$/;
    const password = control.get('password');
    const isValidPassword = passwordRegex.test(password?.value);
    const cmail = control.get('username');
    const isValidUsername = usernameRegex.test(cmail?.value);
    if (isValidUsername /*&& isValidPassword*/) {
      this.isDisabled = false;
    } else {
      this.isDisabled = true;
    }
    const errors: { [key: string]: any } = {};
    if (!isValidUsername) {
      errors['validUsername'] = true;
    }
    if (!isValidPassword) {
      errors['validPassword'] = true;
    }
    return Object.keys(errors).length > 0 ? errors : null;
    
  }

}
