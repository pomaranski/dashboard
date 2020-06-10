import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';

import { LoginRequest } from './../../models/requests/login-request';
import { LoginResponse } from '../../models/responses/login-response';
import { RegisterRequest } from '../../models/requests/RegisterRequest';
import { environment } from 'src/environments/environment';
import { Role } from '../../constants/roles';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    private tokenSubject: BehaviorSubject<string>;
    private roleSubject: BehaviorSubject<string>;
    public token: Observable<string>;
    public role: Observable<string>;

    constructor(private http: HttpClient) {
        this.tokenSubject = new BehaviorSubject<string>(
            localStorage.getItem('token')
        );
        this.roleSubject = new BehaviorSubject<string>(
            localStorage.getItem('role')
        );
        this.token = this.tokenSubject.asObservable();
        this.role = this.roleSubject.asObservable();
    }

    public get currentToken(): string {
        return this.tokenSubject.value;
    }

    public get currentRole(): string {
        return this.roleSubject.value;
    }

    public get isAdmin(): boolean {
        const x = Role[this.currentRole];
        return this.currentRole && Role[this.currentRole] === Role.ROLE_ADMIN;
    }

    private readonly baseUrl = environment.apiBase;

    public login(loginRequest: LoginRequest): Observable<string> {
        const url = `${this.baseUrl}/login`;
        return this.http.post<any>(url, loginRequest).pipe(
            map((loginResponse: LoginResponse) => {
                localStorage.setItem('token', loginResponse.token);
                const role: Role = this.getRole(loginResponse.token);
                localStorage.setItem('role', role);
                this.tokenSubject.next(loginResponse.token);
                this.roleSubject.next(role);
                return loginResponse.token;
            })
        );
    }

    public register(registerRequest: RegisterRequest): Observable<void> {
        const url = `${this.baseUrl}/admin/register`;
        return this.http.post<any>(url, registerRequest);
    }

    public logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        this.tokenSubject.next(null);
        this.roleSubject.next(null);
    }

    private getRole(token: string): Role {
        const tokenData = token.split('.')[1];
        const decodedTokenJsonData = window.atob(tokenData);
        const decodedTokenData = JSON.parse(decodedTokenJsonData);

        const role: string = decodedTokenData.role;

        return Role[role];
    }
}
