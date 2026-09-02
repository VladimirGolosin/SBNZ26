import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface MonthlyWeather {
  month: string;
  avgTemperature: number;
  avgRainfall: number;
}

export interface CostProfitEntry {
  cropId: number;
  cultureName: string;
  status: string;
  cost: number;
  revenue: number;
  profit: number;
}

export interface CostProfitReport {
  entries: CostProfitEntry[];
  totalCost: number;
  totalRevenue: number;
  totalProfit: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient) {}

  getAvailableYears(): Observable<number[]> {
    return this.http.get<number[]>(`${environment.api}reports/weather/years`);
  }

  getWeatherReport(year: number): Observable<MonthlyWeather[]> {
    return this.http.get<MonthlyWeather[]>(`${environment.api}reports/weather`, {
      params: { year }
    });
  }

  getCostProfitReport(userId: number): Observable<CostProfitReport> {
    return this.http.get<CostProfitReport>(`${environment.api}reports/cost-profit`, {
      params: { userId }
    });
  }
}