import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { InstanceService } from 'src/app/core/services/instance.service';
import { Credentials } from 'src/app/core/models/credentials';

@Component({
    selector: 'ngx-instance-page',
    templateUrl: './instance-page.component.html',
})
export class InstancePageComponent implements OnInit {
    @ViewChild('instanceView', { static: true }) instanceView: ElementRef;

    instanceUrl: string = '';

    private login: string = '';
    private password: string = '';
    private instanceId: string = '';

    private readonly LOGIN_TIMEOUT: number = 3000;
    
    constructor(
        private route: ActivatedRoute,
        private instanceService: InstanceService
    ) {}

    ngOnInit(): void {
        this.instanceId = this.route.snapshot.paramMap.get('instanceId');
        this.instanceUrl = this.route.snapshot.paramMap.get('instanceUrl');

        this.loginToInstance().subscribe((_) => {
            setTimeout(() => {
                this.instanceView.nativeElement.contentWindow.postMessage(
                    JSON.stringify({
                        login: this.login,
                        password: this.password,
                    }),
                    this.instanceUrl
                );
            }, this.LOGIN_TIMEOUT);
        });
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
