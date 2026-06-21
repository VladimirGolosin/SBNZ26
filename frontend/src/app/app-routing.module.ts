import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { StartComponent } from './start/start.component';
import { GardenComponent } from './garden/garden.component';
import { ReportsComponent } from './reports/reports.component';

const routes: Routes = [
  {path: "", component: StartComponent},
  {path: "login", component: LoginComponent},
  {path: "register", component: RegistrationComponent},
  {path: "garden", component: GardenComponent},
  {path: "reports", component: ReportsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
