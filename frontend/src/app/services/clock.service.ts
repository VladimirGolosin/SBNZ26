import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface WeatherDayInfo {
  id: number;
  temperature: number;
  rainfall: number;
  date: string;
}

export interface ClockStatus {
  currentDate: string;
  lastReading: WeatherDayInfo | null;
  activeProfile: string;
  weatherMode: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClockService {

  constructor(private http: HttpClient) {}

  getStatus(): Observable<ClockStatus> {
    return this.http.get<ClockStatus>(`${environment.api}clock/status`);
  }

  advanceDayAuto(): Observable<WeatherDayInfo> {
    return this.http.post<WeatherDayInfo>(`${environment.api}clock/advance-day-auto`, null);
  }
}