import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SystemService {

  constructor(private http: HttpClient) {}

  getRecommendedCultures(): Observable<string[]> {
    return this.http.get<string[]>(`${environment.api}system/recommended-cultures`);
  }

  getCriticalCultures(): Observable<string[]> {
    return this.http.get<string[]>(`${environment.api}system/critical-cultures`);
  }
}