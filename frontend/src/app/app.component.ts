import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'scada-front';
  showNavbar: boolean = true;

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const navigationEndEvent = event as NavigationEnd;
        const url = navigationEndEvent.url;
        if (url.includes('garden') || url.includes('reports')) {
          this.showNavbar = true;
        } else {
          this.showNavbar = false;
        }
      }
    });
  }
}
