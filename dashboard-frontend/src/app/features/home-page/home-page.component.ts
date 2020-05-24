import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserApiService } from './../../core/services/api/user-api.service';
import { InstanceResponse } from './../../core/models/responses/instance-response';
import { InstanceService } from 'src/app/core/services/instance.service';
import { Credentials } from 'src/app/core/models/credentials';

@Component({
    selector: 'ngx-home-page',
    templateUrl: './home-page.component.html',
})
export class HomePageComponent implements OnInit {
    constructor(
        private router: Router,
        private userApiService: UserApiService,
        private instanceService: InstanceService
    ) {}

    instances: InstanceResponse[] = [];
    selectedInstance: InstanceResponse;

    ngOnInit(): void {
        this.userApiService.getInstances().subscribe(
            (instances: InstanceResponse[]) => {
                this.instances = instances;
            },
            (error) => {
                console.log(error);
            }
        );
    }

    loginToInstance(instance: InstanceResponse): void {
        this.instanceService.loginToInstance(instance.uuid).subscribe((credentials: Credentials) => {
            console.log(credentials);
            this.router.navigate(['/instances'])
        });
    }

    openRemoveInstanceModal(instance: InstanceResponse): void {
        this.selectedInstance = instance;
    }

    removeInstance(): void {
        this.userApiService.removeInstance(this.selectedInstance.uuid).subscribe(
            _ => this.instances = this.instances.filter(instance => instance.uuid != this.selectedInstance.uuid),
            error => console.log(error)
        );
    }
}
