import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { ReportService, MonthlyWeather, CostProfitReport } from '../services/report.service';
import { SessionService } from '../services/session.service';

Chart.register(...registerables);

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

  @ViewChild('weatherChart') chartRef!: ElementRef<HTMLCanvasElement>;

  years: number[] = [];
  selectedYear: number | null = null;
  weatherData: MonthlyWeather[] = [];
  chart: Chart | null = null;

  costProfitReport: CostProfitReport | null = null;

  monthLabels: string[] = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

  constructor(
    private reportService: ReportService,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    this.reportService.getAvailableYears().subscribe(years => {
      this.years = years;
      if (years.length > 0) {
        this.selectedYear = years[years.length - 1];
        this.loadWeatherReport();
      }
    });

    const user = this.session.getUser();
    if (user) {
      this.reportService.getCostProfitReport(user.id).subscribe(report => {
        this.costProfitReport = report;
      });
    }
  }

  loadWeatherReport(): void {
    if (this.selectedYear === null) {
      return;
    }
    this.reportService.getWeatherReport(this.selectedYear).subscribe(data => {
      this.weatherData = data;
      setTimeout(() => this.renderChart(), 0);
    });
  }

  renderChart(): void {
    if (!this.chartRef) {
      return;
    }
    if (this.chart) {
      this.chart.destroy();
    }
    this.chart = new Chart(this.chartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: this.monthLabels,
        datasets: [
          {
            label: 'Avg Temperature (°C)',
            data: this.weatherData.map(d => d.avgTemperature),
            backgroundColor: '#ff7043'
          },
          {
            label: 'Avg Rainfall (mm)',
            data: this.weatherData.map(d => d.avgRainfall),
            backgroundColor: '#29b6f6'
          }
        ]
      },
      options: {
        responsive: true,
        scales: {
          y: { beginAtZero: true }
        }
      }
    });
  }

  formatEnum(value: string): string {
    const acronyms = ['OK', 'INF'];
    return value
      .split('_')
      .map(word => acronyms.includes(word) ? word : word.charAt(0) + word.slice(1).toLowerCase())
      .join(' ');
  }
}