import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CropService } from '../services/crop.service';
import { CropStateDTO } from '../DTOs/CropStateDTO';

@Component({
  selector: 'app-crop-card',
  templateUrl: './crop-card.component.html',
  styleUrls: ['./crop-card.component.css']
})
export class CropCardComponent {

  @Input() crop!: CropStateDTO;
  @Input() currentDate: string = '';
  @Output() updated = new EventEmitter<void>();

  selectedAction: string = '';
  selectedProblem: string = '';

  iconMap: Record<string, string> = {
    ACTION_DUE: 'assets/icons/actiondue.png',
    NEEDS_IRRIGATION: 'assets/icons/irrigation.png',
    HARVEST_READY: 'assets/icons/harvest.png',
    PROBLEM_SOLUTION: 'assets/icons/problem.png'
  };

  cultureActions: Record<string, string[]> = {
    ONION: ['WEED_REMOVAL', 'FERTILIZATION', 'IRRIGATION'],
    BEANS: ['WEED_REMOVAL', 'FERTILIZATION', 'IRRIGATION'],
    POTATO: ['WEED_REMOVAL', 'HILLING', 'PEST_CONTROL', 'IRRIGATION']
  };

  cultureProblems: Record<string, string[]> = {
    ONION: ['DOWNY_MILDEW', 'ONION_FLY'],
    BEANS: ['BEAN_LEAF_SPOT', 'APHIDS'],
    POTATO: ['POTATO_BLIGHT', 'COLORADO_POTATO_BEETLE']
  };

  constructor(private cropService: CropService) {}

  get availableActions(): string[] {
    return this.cultureActions[this.crop.cultureName] || [];
  }

  get availableProblems(): string[] {
    return this.cultureProblems[this.crop.cultureName] || [];
  }

  get hasHarvestReady(): boolean {
    return this.crop.recommendations.some(r => r.type === 'HARVEST_READY');
  }

  get problemSolutionRecs() {
    return this.crop.recommendations.filter(r => r.type === 'PROBLEM_SOLUTION');
  }

  logAction(): void {
    if (!this.selectedAction) {
      return;
    }
    this.cropService.logAction(this.crop.id, this.selectedAction, this.currentDate).subscribe({
      next: () => this.updated.emit(),
      error: (err) => alert(err?.error || 'Could not log action')
    });
  }

  reportProblem(): void {
    if (!this.selectedProblem) {
      return;
    }
    this.cropService.reportProblem(this.crop.id, this.selectedProblem).subscribe({
      next: () => this.updated.emit(),
      error: (err) => alert(err?.error || 'Could not report problem')
    });
  }

  resolveProblem(problemName: string | null): void {
    if (!problemName) {
      return;
    }
    this.cropService.resolveProblem(this.crop.id, problemName).subscribe({
      next: () => this.updated.emit(),
      error: (err) => alert(err?.error || 'Could not resolve problem')
    });
  }

  collectCrop(): void {
    if (!confirm(`Collect this ${this.crop.cultureName}?`)) {
      return;
    }
    this.cropService.collectCrop(this.crop.id).subscribe({
      next: () => this.updated.emit(),
      error: (err) => alert(err?.error || 'Could not collect crop')
    });
  }
}