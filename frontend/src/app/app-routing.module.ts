import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { StartComponent } from './start/start.component';
import { GardenComponent } from './garden/garden.component';
import { ReportsComponent } from './reports/reports.component';
import { HistoryComponent } from './history/history.component';

const routes: Routes = [
  {path: "", component: StartComponent},
  {path: "login", component: LoginComponent},
  {path: "register", component: RegistrationComponent},
  {path: "garden", component: GardenComponent},
  {path: "reports", component: ReportsComponent},
  {path: "history", component: HistoryComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }