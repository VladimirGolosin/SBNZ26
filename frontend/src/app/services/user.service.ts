import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserDTO } from '../DTOs/UserDTO';
import { CreateUserDTO } from '../DTOs/CreateUserDTO';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  login(dto: UserDTO): Observable<any> {
    return this.http.post(
      `${environment.api}user/login`,
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
    { headers: new HttpHeaders().set("content-type", "application/json") }
  );
}

  logout(): Observable<any> {
    return this.http.post(
      `${environment.api}user/logout`,
      {},
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      }
    );
  }
}