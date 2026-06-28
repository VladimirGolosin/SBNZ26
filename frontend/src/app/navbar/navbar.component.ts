import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService, User } from '../services/session.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  user: User | null = null;

  constructor(
    private router: Router,
    private session: SessionService
  ) {}

  ngOnInit(): void {
    this.session.user$.subscribe(user => {
      this.user = user;
    });
  }

  logout() {
    this.session.clear();
    this.router.navigate(['/login']);
  }

  goToGarden() {
    this.router.navigate(['/garden']);
  }

  goToReports() {
    this.router.navigate(['/reports']);
  }
}