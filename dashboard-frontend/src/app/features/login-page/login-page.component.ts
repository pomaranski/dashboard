import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

import { AuthenticationService } from './../../core/services/api/authentication-api.service';
import { LoginRequest } from 'src/app/core/models/requests/login-request';

@Component({
    selector: 'ngx-login-page',
    templateUrl: './login-page.component.html',
    styleUrls: ['./login-page.component.scss'],
})
export class LoginPageComponent implements OnInit {
    loginForm: FormGroup;
    loading: boolean = false;
    submitted: boolean = false;
    loginFailed: boolean = false;

    private returnUrl: string;

    get form() {
        return this.loginForm.controls;
    }

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService,
        private authenticationService: AuthenticationService
    ) {}

    ngOnInit(): void {
        this.configureForm();
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.loginForm.invalid) {
            return;
        }

        this.loading = true;
        this.authenticationService
            .login({
                login: this.form.login.value,
                password: this.form.password.value,
                key: this.form.key.value,
            } as LoginRequest)
            .pipe(first())
            .subscribe(
                (_) => {
                    this.toastr.success(
                        'Logged in successfully!',
                        'Success',
                        {
                            timeOut: 2000,
                        }
                    );
                    this.router.navigate([this.returnUrl]);
                },
                (_) => {
                    this.loading = false;
                    this.loginFailed = true;
                    this.markAllControlsAsUntouched();
                    this.toastr.error(
                        'Something went wrong while logging!',
                        'Error',
                        {
                            timeOut: 4000,
                        }
                    );
                }
            );
    }

    private configureForm(): void {
        this.loginForm = this.formBuilder.group({
            login: ['', Validators.required],
            password: ['', Validators.required],
            key: ['', Validators.required],
        });
    }

    private markAllControlsAsUntouched(): void {
        Object.keys(this.form).forEach((key) => {
            this.loginForm.get(key).markAsUntouched();
        });
    }
}
