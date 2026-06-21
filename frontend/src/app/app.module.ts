import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RegistrationComponent } from './registration/registration.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import {MatTabsModule} from '@angular/material/tabs';
import {TextFieldModule} from '@angular/cdk/text-field';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import {MatDialogModule} from "@angular/material/dialog";
import { MatSortModule } from '@angular/material/sort';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { StartComponent } from './start/start.component';
import { GardenComponent } from './garden/garden.component';
import { ReportsComponent } from './reports/reports.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavbarComponent,
    RegistrationComponent,
    StartComponent,
    GardenComponent,
    ReportsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatSlideToggleModule,
    MatTabsModule,
    TextFieldModule,
    MatPaginatorModule,
    MatTableModule,
    MatIconModule,
    MatTooltipModule,
    MatDialogModule,
    MatSortModule,
    MatSnackBarModule,
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
