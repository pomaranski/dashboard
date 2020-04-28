import { Component } from '@angular/core';
import { AuthenticationService } from './../../services/api/authentication-api.service';
import { Router } from '@angular/router';

@Component({
    selector: 'ngx-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
    constructor(
        private authenticationService: AuthenticationService,
        private router: Router
    ) {}

    get isLoggedIn(): boolean {
        const currentUser = this.authenticationService.currentUserValue;
        if (currentUser) {
            return true;
        }

        return false;
    }

    logout(): void {
        this.authenticationService.logout();
        this.router.navigate(['/login']);
    }
}
