import { Component, OnInit } from '@angular/core';
import { CropService } from '../services/crop.service';
import { ClockService } from '../services/clock.service';
import { SessionService } from '../services/session.service';
import { CropStateDTO } from '../DTOs/CropStateDTO';

@Component({
  selector: 'app-garden',
  templateUrl: './garden.component.html',
  styleUrls: ['./garden.component.css']
})
export class GardenComponent implements OnInit {

  activeCrops: CropStateDTO[] = [];
  pastCrops: CropStateDTO[] = [];
  currentDate: string = '';

  availableCultures: string[] = ['ONION', 'BEANS', 'POTATO'];
  selectedCulture: string = 'ONION';

  constructor(
    private cropService: CropService,
    private clockService: ClockService,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    this.loadClockStatus();
    this.loadCrops();
  }

  loadClockStatus(): void {
    this.clockService.getStatus().subscribe(status => {
      this.currentDate = status.currentDate;
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

    this.cropService.listCrops(user.id, false).subscribe(crops => {
      this.pastCrops = crops;
    });
  }

  plantCrop(): void {
    const user = this.session.getUser();
    if (!user) {
      return;
    }

    this.cropService.plantCrop(this.selectedCulture, user.id).subscribe({
      next: () => {
        this.loadCrops();
      },
      error: (err) => {
        alert(err?.error || 'Could not plant crop');
      }
    });
  }
}