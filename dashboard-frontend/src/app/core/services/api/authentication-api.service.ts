import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';

import { User } from '../../models/user';
import { LoginRequest } from './../../models/requests/login-request';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    // TODO: set to private
    public currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;

    constructor(private http: HttpClient) {
        this.currentUserSubject = new BehaviorSubject<User>(
            JSON.parse(localStorage.getItem('currentUser'))
        );
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    private readonly baseUrl = environment.apiBase;

    public login(loginRequest: LoginRequest): Observable<any> {
        const url = `${this.baseUrl}/login`;
        return this.http.post<any>(url, loginRequest).pipe(
            // TODO: replace with response ang get token from header
            map((user: User) => {
                localStorage.setItem('currentUser', JSON.stringify(user));
                this.currentUserSubject.next(user);
                return user;
            })
        );
    }

    public logout(): void {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }
}
