import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { SystemService } from '../services/system.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private router: Router, private UserService: UserService, private SystemService: SystemService) { }

  ngOnInit(): void {
  }

  goToManageTags() {}

  goToTrending() {}

  goToReports() {}

  goToRegister() {}

  startSimulation(){
    this.SystemService.startSystemSimulation().subscribe({
      next: result => {
        // alert(result.message);
        console.log(result.message);
      },
      error: err => {
        console.log(err);
        alert(err?.error?.message || JSON.stringify(err));
      }
    })
  }
  stopSimulation(){
    this.SystemService.stopSystemSimulation().subscribe({
      next: result => {
        // alert(result.message);
        console.log(result.message);
      },
      error: err => {
        console.log(err);
        alert(err?.error?.message || JSON.stringify(err));
      }
    })
  }
  restartSimulation(){
    this.SystemService.restartSystemSimulation().subscribe({
      next: result => {
        // alert(result.message);
        console.log(result.message);
      },
      error: err => {
        console.log(err);
        alert(err?.error?.message || JSON.stringify(err));
      }
    })
  }

  logout() {
    this.UserService.logout().subscribe({
      next: result => {
        // alert(result.message);
        console.log(result.message);
        this.router.navigate(['']).then(()=>{location.reload();});
      },
      error: err => {
        console.log(err);
        alert(err?.error?.message || JSON.stringify(err));
      }
    })
  }

}
