import { Component, OnInit } from '@angular/core';
import { CropService } from '../services/crop.service';
import { SessionService } from '../services/session.service';
import { CropStateDTO } from '../DTOs/CropStateDTO';

interface YearGroup {
  year: string;
  crops: CropStateDTO[];
}

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {

  yearGroups: YearGroup[] = [];

  constructor(
    private cropService: CropService,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    const user = this.session.getUser();
    if (!user) {
      return;
    }
    this.cropService.listCrops(user.id, false).subscribe(crops => {
      this.yearGroups = this.groupByYear(crops);
    });
  }

  private groupByYear(crops: CropStateDTO[]): YearGroup[] {
    const map = new Map<string, CropStateDTO[]>();

    for (const crop of crops) {
      const year = crop.plantedDate ? crop.plantedDate.substring(0, 4) : 'Unknown';
      if (!map.has(year)) {
        map.set(year, []);
      }
      map.get(year)!.push(crop);
    }

    return Array.from(map.entries())
      .map(([year, crops]) => ({ year, crops }))
      .sort((a, b) => b.year.localeCompare(a.year));
  }
}