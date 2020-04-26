import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';

@Injectable()
export class OwnerApiService {
  constructor(private http: HttpClient) { }

  private readonly baseUrl = environment.apiBase + '/owner';

  public addInstance(request: any): Observable<any> {
    const url = `${this.baseUrl}/addInstance`;
    return this.http.post<any>(url, request);
  }

  public removeInstance(instanceId: any): Observable<any> {
    const url = `${this.baseUrl}/removeInstance/${instanceId}`;
    return this.http.post<any>(url, null);
  }
}
