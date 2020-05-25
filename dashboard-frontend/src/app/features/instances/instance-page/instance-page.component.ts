import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

import { InstanceService } from 'src/app/core/services/instance.service';
import { Credentials } from 'src/app/core/models/credentials';

@Component({
    selector: 'ngx-instance-page',
    templateUrl: './instance-page.component.html',
    styleUrls: ['./instance-page.component.scss'],
})
export class InstancePageComponent implements OnInit {
    instanceUrl: string = '';
    isLoading: boolean = false;
    height: number = 0;
    width: number = 0;

    private login: string = '';
    private password: string = '';
    private instanceId: string = '';

    private readonly LOGIN_TIMEOUT: number = 3000;

    constructor(
        private route: ActivatedRoute,
        private toastr: ToastrService,
        private instanceService: InstanceService
    ) {}

    ngOnInit(): void {
        this.instanceId = this.route.snapshot.paramMap.get('instanceId');
        this.instanceUrl = this.route.snapshot.paramMap.get('instanceUrl');
        this.isLoading = true;

        this.resizeInstanceView();

        this.loginToInstance().subscribe(
            (_) => {
                setTimeout(() => {
                    const instanceView = document.getElementById('instance-view') as any;
                    instanceView.contentWindow.postMessage(
                        JSON.stringify({
                            login: this.login,
                            password: this.password,
                        }),
                        this.instanceUrl
                    );
                    setTimeout(() => (this.isLoading = false), 1000);
                }, this.LOGIN_TIMEOUT);
            },
            (_) => {
                this.isLoading = false;
                this.toastr.error(
                    'Something went wrong while logging to instance!',
                    'Error',
                    {
                        timeOut: 4000,
                    }
                );
            }
        );
    }

    private resizeInstanceView(): void {
        this.height = 0.8 * window.innerHeight;
        this.width = 0.9 * window.innerWidth;
    }

    private loginToInstance(): Observable<void> {
        return this.instanceService.loginToInstance(this.instanceId).pipe(
            map((credentials: Credentials) => {
                this.login = credentials.login;
                this.password = credentials.password;
            })
        );
    }
}
