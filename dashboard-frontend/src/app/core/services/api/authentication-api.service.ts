import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';

import { LoginRequest } from './../../models/requests/login-request';
import { LoginResponse } from '../../models/responses/login-response';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    private tokenSubject: BehaviorSubject<string>;
    public token: Observable<string>;

    constructor(private http: HttpClient) {
        this.tokenSubject = new BehaviorSubject<string>(
            localStorage.getItem('token')
        );
        this.token = this.tokenSubject.asObservable();
    }

    public get currentToken(): string {
        return this.tokenSubject.value;
    }

    private readonly baseUrl = environment.apiBase;

    public login(loginRequest: LoginRequest): Observable<any> {
        const url = `${this.baseUrl}/login`;
        return this.http.post<any>(url, loginRequest).pipe(
            map((loginResponse: LoginResponse) => {
                localStorage.setItem('token', loginResponse.token);
                this.tokenSubject.next(loginResponse.token);
                return loginResponse.token;
            })
        );
    }

    public logout(): void {
        localStorage.removeItem('token');
        this.tokenSubject.next(null);
    }
}
