import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators, FormGroup, FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

import { RegisterRequest } from 'src/app/core/models/requests/RegisterRequest';
import { AuthenticationService } from 'src/app/core/services/api/authentication-api.service';

@Component({
    selector: 'ngx-register-owner-page',
    templateUrl: './register-owner-page.component.html',
})
export class RegisterOwnerPageComponent implements OnInit {
    registerForm: FormGroup;
    loading: boolean = false;
    submitted: boolean = false;
    registerFailed: boolean = false;

    private returnUrl: string;

    get form() {
        return this.registerForm.controls;
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
        if (this.registerForm.invalid) {
            return;
        }

        this.loading = true;
        this.authenticationService
            .register({
                login: this.form.login.value,
                hashedPassword: this.form.password.value,
                hashedKey: this.form.key.value,
            } as RegisterRequest)
            .subscribe(
                (_) => {
                    this.toastr.success(
                        'Registered successfully!',
                        'Success',
                        {
                            timeOut: 2000,
                        }
                    );
                    this.router.navigate([this.returnUrl]);
                },
                (_) => {
                    this.loading = false;
                    this.registerFailed = true;
                    this.markAllControlsAsUntouched();
                    this.toastr.error(
                        'Something went wrong while registering!',
                        'Error',
                        {
                            timeOut: 4000,
                        }
                    );
                }
            );
    }

    private configureForm(): void {
        this.registerForm = this.formBuilder.group({
            login: ['', Validators.required],
            password: ['', Validators.required],
            key: ['', Validators.required],
        });
    }

    private markAllControlsAsUntouched(): void {
        Object.keys(this.form).forEach((key) => {
            this.registerForm.get(key).markAsUntouched();
        });
    }
}
