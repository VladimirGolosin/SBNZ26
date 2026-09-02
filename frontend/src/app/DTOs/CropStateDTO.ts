export interface Recommendation {
  type: 'ACTION_DUE' | 'NEEDS_IRRIGATION' | 'HARVEST_READY' | 'PROBLEM_SOLUTION';
  message: string;
  actionName: string | null;
  solutionName: string | null;
  problemName: string | null;
}

export interface CropStateDTO {
  id: number;
  cultureName: string;
  level: number;
  status: string;
  size: number;
  plantedDate: string | null;
  recommendations: Recommendation[];
}