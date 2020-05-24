import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserApiService } from './../../core/services/api/user-api.service';
import { InstanceResponse } from './../../core/models/responses/instance-response';

@Component({
    selector: 'ngx-home-page',
    templateUrl: './home-page.component.html',
})
export class HomePageComponent implements OnInit {
    constructor(
        private router: Router,
        private userApiService: UserApiService,
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

    removeInstance(): void {
        this.userApiService.removeInstance(this.selectedInstance.uuid).subscribe(
            _ => this.instances = this.instances.filter(instance => instance.uuid != this.selectedInstance.uuid),
            error => console.log(error)
        );
    }
}
