import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CreateUserDTO } from '../DTOs/CreateUserDTO';
import { LoginDTO } from '../DTOs/LoginDTO';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  login(dto: LoginDTO): Observable<any> {
    return this.http.post(
      `${environment.api}auth/login`,
      dto,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      }
    );
  }

  register(dto: CreateUserDTO): Observable<any> {
    return this.http.post(
      `${environment.api}auth/register`,
      dto,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      }
    );
  }

  logout(): Observable<any> {
    return this.http.post(
      `${environment.api}auth/logout`,
      {},
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      }
    );
  }
}