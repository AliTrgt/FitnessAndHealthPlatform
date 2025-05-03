import { CommonModule } from '@angular/common';
import { AfterViewChecked, AfterViewInit, Component, effect, OnInit, signal, ViewChild, viewChild, ɵunwrapWritableSignal } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { LoginComponent } from "./components/login/login.component";
import { NavbarComponent } from "./components/navbar/navbar.component";
import { HomePageComponent } from './components/home-page/home-page.component';
import { User } from './model/user';
import { AuthUserService } from './service/security/auth-user.service';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
}
