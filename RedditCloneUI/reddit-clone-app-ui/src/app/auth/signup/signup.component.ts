import { query } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../shared/auth.service';
import { SignupRequestPayload } from './signup-request.payload';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupForm: FormGroup;
  signupReqPayload: SignupRequestPayload;

  constructor(
    private authService: AuthService, private toastr: ToastrService,
    private router: Router
  ) {
    this.signupReqPayload = {
      username: '',
      email: '',
      password: ''
    }
  }

  ngOnInit() {
    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    });
  }

  signup() {
    this.signupReqPayload.email = this.signupForm.get('email')?.value;
    this.signupReqPayload.username = this.signupForm.get('username')?.value;
    this.signupReqPayload.password = this.signupForm.get('password')?.value;

    this.authService
      .signup(this.signupReqPayload)
      .subscribe((data) => {
        console.log(data);
        this.router.navigate(['/login'], { queryParams: { registered: 'true' }});
      }, () => {
        this.toastr.error('Registration Failed. Please try again.');
      });
  }

}
