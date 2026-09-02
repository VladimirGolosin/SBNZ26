import { Component, OnInit } from '@angular/core';
import { CropService } from '../services/crop.service';
import { SessionService } from '../services/session.service';
import { CropStateDTO } from '../DTOs/CropStateDTO';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {

  pastCrops: CropStateDTO[] = [];

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
      this.pastCrops = crops;
    });
  }
}