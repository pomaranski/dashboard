import { Injectable } from '@angular/core';
import {
    Router,
    CanActivate,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
    Data,
} from '@angular/router';

import { AuthenticationService } from '../services/api/authentication-api.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentUser = this.authenticationService.currentToken;
        if (currentUser) {
            if(this.isAuthorized(route.data)) {
                return true;
            } else {
                this.router.navigate(['/home']);
                return false;
            }
        }

        this.router.navigate(['/login'], {
            queryParams: { returnUrl: state.url },
        });
        return false;
    }

    private isAuthorized(data: Data): boolean {
        return !data.roles || data.roles.includes(this.authenticationService.currentRole);
    }
}
