import { Component, Input } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

import { UserApiService } from 'src/app/core/services/api/user-api.service';
import { ExecuteCommandRequest } from 'src/app/core/models/requests/execute-command-request';

@Component({
    selector: 'ngx-execute-command-modal',
    templateUrl: './execute-command-modal.component.html',
})
export class ExecuteCommandModalComponent {
    constructor(
        private toastr: ToastrService,
        private userApiService: UserApiService
    ) {}

    @Input() instanceId: string;
    command: string = '';

    execute(): void {
        const request: ExecuteCommandRequest = {
            command: this.command,
        } as ExecuteCommandRequest;
        this.userApiService.executeCommand(this.instanceId, request).subscribe(
            (_) => {
                this.command = '';
                this.toastr.success(
                    'Command executed successfully!',
                    'Success',
                    {
                        timeOut: 2000,
                    }
                );
            },
            (_) =>
                this.toastr.error(
                    'Something went wrong while executing command!',
                    'Error',
                    {
                        timeOut: 4000,
                    }
                )
        );
    }
}
