import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SystemService {

  constructor(private http: HttpClient) { }

  postAddresses():Observable<any> {
    return this.http.get(`${environment.api}system/addresses`);
  }
  startSystemSimulation():Observable<any>{
    return this.http.get(`${environment.api}system/start`)
  }
  stopSystemSimulation():Observable<any>{
    return this.http.get(`${environment.api}system/stop`)
  }
  restartSystemSimulation():Observable<any>{
    return this.http.get(`${environment.api}system/restart`)
  }
}