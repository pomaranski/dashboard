import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { InstanceResponse } from '../../models/responses/instance-response';
import { InstanceRequest } from '../../models/requests/instance-request';

@Injectable({ providedIn: 'root' })
export class UserApiService {
  constructor(private http: HttpClient) { }

  private readonly baseUrl = environment.apiBase + '/user';

  public loginToInstance(instanceId: string): Observable<void> {
    const url = `${this.baseUrl}/instance/${instanceId}/login`;
    return this.http.post<void>(url, null);
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
