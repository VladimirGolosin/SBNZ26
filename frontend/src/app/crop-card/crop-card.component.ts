import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
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
  @Input() readOnly: boolean = false;
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
    POTATO: ['WEED_REMOVAL', 'HILLING', 'PEST_CONTROL', 'IRRIGATION'],
    TOMATO: ['WEED_REMOVAL', 'TYING', 'PEST_CONTROL', 'IRRIGATION'],
    ZUCCINI: ['WEED_REMOVAL', 'FERTILIZATION', 'IRRIGATION'],
    CORN: ['WEED_REMOVAL', 'FERTILIZATION', 'IRRIGATION'],
    GRAPE: ['PRUNING', 'COPPER_SULFATE_SPRAY', 'PEST_CONTROL', 'IRRIGATION'],
    WATERMELON: ['WEED_REMOVAL', 'FERTILIZATION', 'IRRIGATION']
  };

  cultureProblems: Record<string, string[]> = {
    ONION: ['DOWNY_MILDEW', 'ONION_FLY'],
    BEANS: ['BEAN_LEAF_SPOT', 'APHIDS'],
    POTATO: ['POTATO_BLIGHT', 'COLORADO_POTATO_BEETLE'],
    TOMATO: ['TOMATO_BLIGHT', 'APHIDS'],
    ZUCCINI: ['POWDERY_MILDEW', 'APHIDS'],
    CORN: ['CORN_MOLD', 'CORN_BORER'],
    GRAPE: ['GRAPE_POWDERY_MILDEW', 'GRAPE_MOTH'],
    WATERMELON: ['POWDERY_MILDEW', 'APHIDS']
  };

  cultureMaxLevel: Record<string, number> = {
    ONION: 3,
    BEANS: 3,
    POTATO: 4,
    TOMATO: 4,
    ZUCCINI: 3,
    CORN: 3,
    GRAPE: 4,
    WATERMELON: 3
  };

  constructor(private cropService: CropService, private snackBar: MatSnackBar) {}

  get availableActions(): string[] {
    return this.cultureActions[this.crop.cultureName] || [];
  }

  get availableProblems(): string[] {
    return this.cultureProblems[this.crop.cultureName] || [];
  }

  get maxLevel(): number {
    return this.cultureMaxLevel[this.crop.cultureName] || 3;
  }

  get hasHarvestReady(): boolean {
    return this.crop.recommendations.some(r => r.type === 'HARVEST_READY');
  }

  get problemSolutionRecs() {
    return this.crop.recommendations.filter(r => r.type === 'PROBLEM_SOLUTION');
  }

  formatEnum(value: string): string {
    const acronyms = ['OK', 'INF'];
    return value
      .split('_')
      .map(word => acronyms.includes(word) ? word : word.charAt(0) + word.slice(1).toLowerCase())
      .join(' ');
  }

  private notify(message: string): void {
    this.snackBar.open(message, 'Close', { duration: 3000 });
  }

  logAction(): void {
    if (!this.selectedAction) {
      return;
    }
    this.cropService.logAction(this.crop.id, this.selectedAction, this.currentDate).subscribe({
      next: () => {
        this.notify('Action logged');
        this.updated.emit();
      },
      error: (err) => alert(err?.error || 'Could not log action')
    });
  }

  reportProblem(): void {
    if (!this.selectedProblem) {
      return;
    }
    this.cropService.reportProblem(this.crop.id, this.selectedProblem).subscribe({
      next: () => {
        this.notify('Problem reported');
        this.updated.emit();
      },
      error: (err) => alert(err?.error || 'Could not report problem')
    });
  }

  resolveProblem(problemName: string | null): void {
    if (!problemName) {
      return;
    }
    this.cropService.resolveProblem(this.crop.id, problemName).subscribe({
      next: () => {
        this.notify('Problem resolved');
        this.updated.emit();
      },
      error: (err) => alert(err?.error || 'Could not resolve problem')
    });
  }

  collectCrop(): void {
    if (!confirm(`Collect this ${this.crop.cultureName}?`)) {
      return;
    }
    this.cropService.collectCrop(this.crop.id, false).subscribe({
      next: () => {
        this.notify('Crop collected');
        this.updated.emit();
      },
      error: (err) => {
        const message: string = err?.error || '';
        if (message.includes('has not completed all growth stages')) {
          if (confirm(message + '\n\nCollect anyway?')) {
            this.cropService.collectCrop(this.crop.id, true).subscribe({
              next: () => {
                this.notify('Crop collected as INF (incomplete growth)');
                this.updated.emit();
              },
              error: (err2) => alert(err2?.error || 'Could not collect crop')
            });
          }
        } else {
          alert(message || 'Could not collect crop');
        }
      }
    });
  }

  failCrop(): void {
    if (!confirm(`Mark this ${this.crop.cultureName} as failed?`)) {
      return;
    }
    this.cropService.failCrop(this.crop.id).subscribe({
      next: () => {
        this.notify('Crop marked as failed');
        this.updated.emit();
      },
      error: (err) => alert(err?.error || 'Could not mark crop as failed')
    });
  }
}