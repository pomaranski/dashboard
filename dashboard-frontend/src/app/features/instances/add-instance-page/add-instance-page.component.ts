import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { UserApiService } from './../../../core/services/api/user-api.service';
import { InstanceRequest } from './../../../core/models/requests/instance-request';

@Component({
    selector: 'ngx-add-instance-page',
    templateUrl: './add-instance-page.component.html',
})
export class AddInstancePageComponent implements OnInit {
    addInstanceForm: FormGroup;
    loading: boolean = false;
    submitted: boolean = false;
    requestFailed: boolean = false;

    get form() {
        return this.addInstanceForm.controls;
    }

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private toastr: ToastrService,
        private userApiService: UserApiService
    ) {}

    ngOnInit(): void {
        this.configureForm();
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.addInstanceForm.invalid) {
            return;
        }

        this.loading = true;
        this.userApiService
            .addInstance({
                name: this.form.name.value,
                httpUri: this.form.httpUri.value,
                instanceLogin: this.form.instanceLogin.value,
                instancePassword: this.form.instancePassword.value,
                sshUri: this.form.sshUri.value,
                hostLogin: this.form.hostLogin.value,
                hostPassword: this.form.hostPassword.value,
            } as InstanceRequest)
            .subscribe(
                (_) => {
                    this.toastr.success(
                        'Instance added successfully!',
                        'Success',
                        {
                            timeOut: 2000,
                        }
                    );
                    this.router.navigate(['home']);
                },
                (_) => {
                    this.loading = false;
                    this.requestFailed = true;
                    this.markAllControlsAsUntouched();
                    this.toastr.error(
                        'Something went wrong while adding instance!',
                        'Error',
                        {
                            timeOut: 4000,
                        }
                    );
                }
            );
    }

    private configureForm(): void {
        this.addInstanceForm = this.formBuilder.group({
            name: ['', Validators.required],
            httpUri: ['', Validators.required],
            instanceLogin: ['', Validators.required],
            instancePassword: ['', Validators.required],
            sshUri: ['', Validators.required],
            hostLogin: ['', Validators.required],
            hostPassword: ['', Validators.required],
        });
    }

    private markAllControlsAsUntouched(): void {
        Object.keys(this.form).forEach((key) => {
            this.addInstanceForm.get(key).markAsUntouched();
        });
    }
}
