import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CropStateDTO } from '../DTOs/CropStateDTO';

@Injectable({
  providedIn: 'root'
})
export class CropService {

  constructor(private http: HttpClient) {}

  plantCrop(culture: string, userId: number): Observable<number> {
    return this.http.post<number>(`${environment.api}crops/plant`, null, {
      params: { culture, userId }
    });
  }

  getCrop(id: number): Observable<CropStateDTO> {
    return this.http.get<CropStateDTO>(`${environment.api}crops/${id}`);
  }

  listCrops(userId: number, active: boolean): Observable<CropStateDTO[]> {
    return this.http.get<CropStateDTO[]>(`${environment.api}crops`, {
      params: { userId, active }
    });
  }

  logAction(cropId: number, action: string, date: string): Observable<CropStateDTO> {
    return this.http.post<CropStateDTO>(`${environment.api}crops/${cropId}/actions`, null, {
      params: { action, date }
    });
  }

  reportProblem(cropId: number, problem: string): Observable<CropStateDTO> {
    return this.http.post<CropStateDTO>(`${environment.api}crops/${cropId}/problems`, null, {
      params: { problem }
    });
  }

  markProblemResolving(cropId: number, problemId: number): Observable<CropStateDTO> {
    return this.http.post<CropStateDTO>(`${environment.api}crops/${cropId}/problems/${problemId}/resolving`, null);
  }

  markProblemResolved(cropId: number, problemId: number): Observable<CropStateDTO> {
    return this.http.post<CropStateDTO>(`${environment.api}crops/${cropId}/problems/${problemId}/resolved`, null);
  }

  collectCrop(cropId: number): Observable<CropStateDTO> {
    return this.http.post<CropStateDTO>(`${environment.api}crops/${cropId}/collect`, null);
  }
}