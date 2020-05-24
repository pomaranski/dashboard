import { Component, Input, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'ngx-confirm-modal',
    templateUrl: './confirm-modal.component.html',
})
export class ConfirmModalComponent {
    @Input() title: string;
    @Output() onConfirm = new EventEmitter<any>();

    confirm(): void {
        this.onConfirm.emit(event);
    }
}
