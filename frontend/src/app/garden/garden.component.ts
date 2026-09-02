import { Component, OnInit } from '@angular/core';
import { CropService } from '../services/crop.service';
import { ClockService } from '../services/clock.service';
import { SystemService } from '../services/system.service';
import { SessionService } from '../services/session.service';
import { CropStateDTO } from '../DTOs/CropStateDTO';

@Component({
  selector: 'app-garden',
  templateUrl: './garden.component.html',
  styleUrls: ['./garden.component.css']
})
export class GardenComponent implements OnInit {

  activeCrops: CropStateDTO[] = [];
  currentDate: string = '';
  activeProfile: string = '';
  weatherMode: string = '';

  recommendedCultures: string[] = [];
  criticalCultures: string[] = [];

  selectedCulture: string = '';
  plantSize: number = 10;

  constructor(
    private cropService: CropService,
    private clockService: ClockService,
    private systemService: SystemService,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    this.loadClockStatus();
    this.loadCrops();
    this.loadSystemInfo();
  }

  loadClockStatus(): void {
    this.clockService.getStatus().subscribe(status => {
      this.currentDate = status.currentDate;
      this.activeProfile = status.activeProfile;
      this.weatherMode = status.weatherMode;
    });
  }

  loadSystemInfo(): void {
    this.systemService.getRecommendedCultures().subscribe(cultures => {
      this.recommendedCultures = cultures;
      if (cultures.length > 0 && !cultures.includes(this.selectedCulture)) {
        this.selectedCulture = cultures[0];
      }
    });
    this.systemService.getCriticalCultures().subscribe(cultures => {
      this.criticalCultures = cultures;
    });
  }

  loadCrops(): void {
    const user = this.session.getUser();
    if (!user) {
      return;
    }

    this.cropService.listCrops(user.id, true).subscribe(crops => {
      this.activeCrops = crops;
    });
  }

    formatEnum(value: string): string {
    const acronyms = ['OK', 'INF'];
    return value
      .split('_')
      .map(word => acronyms.includes(word) ? word : word.charAt(0) + word.slice(1).toLowerCase())
      .join(' ');
  }

  plantCrop(): void {
    const user = this.session.getUser();
    if (!user) {
      return;
    }

    if (!this.selectedCulture) {
      alert('No culture is plantable this month.');
      return;
    }

    if (!this.plantSize || this.plantSize <= 0) {
      alert('Please enter a valid size value.');
      return;
    }

    if (!confirm(`Are you sure you want to plant ${this.selectedCulture}?`)) {
      return;
    }

    this.cropService.plantCrop(this.selectedCulture, user.id, this.plantSize, 1).subscribe({
      next: () => {
        this.loadCrops();
        this.loadSystemInfo();
      },
      error: (err) => {
        alert(err?.error || 'Could not plant crop');
      }
    });
  }
}