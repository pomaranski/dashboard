import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// import { map } from 'rxjs/operators';

import { environment } from 'src/environments/environment';
import { InstanceResponse } from '../../models/responses/instance-response';
import { InstanceRequest } from '../../models/requests/instance-request';
// import { LoginToInstanceResponse } from '../../models/responses/login-to-instance-response';
import { ExecuteCommandRequest } from '../../models/requests/execute-command-request';
import { CredentialsRequest } from '../../models/requests/credentials-request';
import { CredentialsResponse } from '../../models/responses/credentials-response';

@Injectable({ providedIn: 'root' })
export class UserApiService {

    constructor(private http: HttpClient) { }

    private readonly baseUrl = environment.apiBase + '/user';

    // public loginToInstance(instanceId: string): Observable<LoginToInstanceResponse> {
    //     const url = `${this.baseUrl}/instance/${instanceId}/login`;
    //     return this.http.post<LoginToInstanceResponse>(url, null).pipe(
    //         map((response: LoginToInstanceResponse) => {
    //             return response;
    //         })
    //     );
    // }

    public getInstances(): Observable<InstanceResponse[]> {
        const url = `${this.baseUrl}/instance`;
        return this.http.get<InstanceResponse[]>(url);
    }

    public addInstance(request: InstanceRequest): Observable<InstanceResponse> {
        const url = `${this.baseUrl}/instance`;
        return this.http.post<InstanceResponse>(url, request);
    }

    public removeInstance(instanceId: string): Observable<void> {
        const url = `${this.baseUrl}/instance/${instanceId}`;
        return this.http.delete<void>(url);
    }

    public executeCommand(instanceId: string, command: ExecuteCommandRequest): Observable<void> {
        const url = `${this.baseUrl}/instance/${instanceId}/execute`;
        return this.http.post<void>(url, command);
    }

    public getCredentials(instanceId: string, credentials: CredentialsRequest): Observable<CredentialsResponse> {
        const url = `${this.baseUrl}/instance/${instanceId}/credentials`;
        return this.http.post<CredentialsResponse>(url, credentials);
    }
}
