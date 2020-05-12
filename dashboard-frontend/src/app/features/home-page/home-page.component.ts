import { Component, OnInit } from '@angular/core';
import { UserApiService } from './../../core/services/api/user-api.service';
import { InstanceResponse } from './../../core/models/responses/instance-response';
import { Router } from '@angular/router';

@Component({
    selector: 'ngx-home-page',
    templateUrl: './home-page.component.html',
})
export class HomePageComponent implements OnInit {
    constructor(private router: Router, private userApiService: UserApiService) {}

    instances: InstanceResponse[] = [];

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
        this.userApiService.loginToInstance(instance.uuid).subscribe(
            (data) => {
                console.log(data);
                this.router.navigate(['/instances']);
            },
            (error) => {
                console.log(error);
            }
        )
    }
}
