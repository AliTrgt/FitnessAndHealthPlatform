import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthUserService } from '../../service/security/auth-user.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule,RouterLink],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {


  constructor(private authUserService:AuthUserService){}

  isLoggedIn() : boolean {
      return this.authUserService.isLoggedIn();
  }

}
