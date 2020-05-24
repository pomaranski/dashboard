import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class AdminApiService {

    constructor(private http: HttpClient) { }

    private readonly baseUrl = environment.apiBase + '/admin';

    public getUnassigned(): Observable<string[]> {
        const url = `${this.baseUrl}/instance/unassigned`;
        return this.http.get<string[]>(url);
    }
}
