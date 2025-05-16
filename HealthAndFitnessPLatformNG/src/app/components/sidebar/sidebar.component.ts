import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, Signal } from '@angular/core';
import { AuthUserService } from '../../service/security/auth-user.service';
import { RouterLink } from '@angular/router';
import { User } from '../../model/user';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  user!: User;
  authRole!:string[];
  public authUserService = inject(AuthUserService);
  currentUser: Signal<User | null> = this.authUserService.currentUser;
  constructor() {
    if (typeof window != 'undefined') {
      const stringUser = localStorage.getItem('currentUser');
      if (stringUser) {
        this.user = JSON.parse(stringUser);
      }
    }
  }

  ngOnInit() {
      this.getRole();
  }

  isLoggedIn(): boolean {
    return this.authUserService.isLoggedIn();
  }

  getRole(){
    this.authRole = this.currentUser()?.authorities.map(role => role.role) || [];
  }


}
