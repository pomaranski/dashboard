import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

import { AdminApiService } from 'src/app/core/services/api/admin-api.service';

@Component({
    selector: 'ngx-unassigned-instances-page',
    templateUrl: './unassigned-instances-page.component.html',
    styleUrls: ['./unassigned-instances-page.component.scss'],
})
export class UnassignedInstancesComponent implements OnInit {
    constructor(
        private toastr: ToastrService,
        private adminApiService: AdminApiService
    ) {}

    instanceUris: string[] = [];

    ngOnInit(): void {
        this.adminApiService
            .getUnassigned()
            .subscribe((instanceUris: string[]) => {
                this.instanceUris = instanceUris;
            });
    }

    copyToClipboard(uri: string): void {
        document.addEventListener('copy', (e: ClipboardEvent) => {
            e.clipboardData.setData('text/plain', uri);
            e.preventDefault();
            document.removeEventListener('copy', null);
        });
        document.execCommand('copy');

        this.toastr.info('Uri copied! ', 'Information', {
            timeOut: 1500,
        });
    }
}
