import { CommonModule, NgLocaleLocalization } from '@angular/common';
import { Component, effect, inject, Signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthUserService } from '../../service/security/auth-user.service';
import { User } from '../../model/user';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink,CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  user!:User;
  public authUserService = inject(AuthUserService);
  currentUser : Signal<User | null> = this.authUserService.currentUser;
  constructor(){
    if(typeof window != 'undefined'){
      const stringUser = localStorage.getItem('currentUser');
        if(stringUser){
            this.user = JSON.parse(stringUser);
        }
    }   
  }
  isLoggedIn() : boolean{
      return this.authUserService.isLoggedIn();
  }

  

}
