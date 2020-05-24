import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { UserApiService } from './../../core/services/api/user-api.service';
import { InstanceResponse } from './../../core/models/responses/instance-response';

@Component({
    selector: 'ngx-home-page',
    templateUrl: './home-page.component.html',
})
export class HomePageComponent implements OnInit {
    constructor(
        private router: Router,
        private toastr: ToastrService,
        private userApiService: UserApiService
    ) {}

    instances: InstanceResponse[] = [];
    selectedInstance: InstanceResponse;
    selectedInstanceId: string;

    ngOnInit(): void {
        this.userApiService.getInstances().subscribe(
            (instances: InstanceResponse[]) => {
                this.instances = instances;
            },
            (_) => {
                this.toastr.error(
                    'Something went wrong while fetching instances!',
                    'Error',
                    {
                        timeOut: 4000,
                    }
                );
            }
        );
    }

    loginToInstance(instance: InstanceResponse): void {
        this.router.navigate([
            '/instances',
            {
                instanceId: instance.uuid,
                instanceUrl: instance.httpUri,
            },
        ]);
    }

    openRemoveInstanceModal(instance: InstanceResponse): void {
        this.selectedInstance = instance;
    }

    openExecuteCommandModal(instance: InstanceResponse): void {
        this.selectedInstanceId = instance.uuid;
    }

    removeInstance(): void {
        this.userApiService
            .removeInstance(this.selectedInstance.uuid)
            .subscribe(
                (_) => {
                    this.instances = this.instances.filter(
                        (instance: InstanceResponse) =>
                            instance.uuid != this.selectedInstance.uuid
                    );
                    this.toastr.success(
                        'Deleted instance successfully!',
                        'Success',
                        {
                            timeOut: 2000,
                        }
                    );
                },
                (_) =>
                    this.toastr.error(
                        'Something went wrong while removing instance!',
                        'Error',
                        {
                            timeOut: 4000,
                        }
                    )
            );
    }
}
