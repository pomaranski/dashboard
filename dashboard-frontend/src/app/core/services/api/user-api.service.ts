import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from 'src/environments/environment';
import { InstanceResponse } from '../../models/responses/instance-response';
import { InstanceRequest } from '../../models/requests/instance-request';
import { LoginToInstanceResponse } from '../../models/responses/login-to-instance-response';
import { CookieService } from 'ngx-cookie-service';

@Injectable({ providedIn: 'root' })
export class UserApiService {

    constructor(
        private http: HttpClient,
        private cookieService: CookieService
    ) {}

    private readonly baseUrl = environment.apiBase + '/user';

    public loginToInstance(
        instanceId: string
    ): Observable<LoginToInstanceResponse> {
        const url = `${this.baseUrl}/instance/${instanceId}/login`;
        return this.http.post<LoginToInstanceResponse>(url, null).pipe(
            map((response: LoginToInstanceResponse) => {
                return response;
            })
        );
    }

    public getInstances(): Observable<InstanceResponse[]> {
        const url = `${this.baseUrl}/instance`;
        return this.http.get<InstanceResponse[]>(url);
    }

    public addInstance(request: InstanceRequest): Observable<InstanceResponse> {
        const url = `${this.baseUrl}/instance`;
        return this.http.post<InstanceResponse>(url, request);
    }

    public removeInstances(instanceId: string): Observable<void> {
        const url = `${this.baseUrl}/instance/${instanceId}`;
        return this.http.delete<void>(url);
    }
}
