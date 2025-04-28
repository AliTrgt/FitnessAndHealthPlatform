import { Component, effect, inject, OnInit, Signal, signal } from '@angular/core';
import { FormControl,FormGroup,FormsModule,ReactiveFormsModule, Validators} from '@angular/forms';
import { UserService } from '../../service/user/user.service';
import { AuthUserService } from '../../service/security/auth-user.service';

import { User } from '../../model/user';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule,RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent{
  private loginService = inject(AuthUserService);
  user : Signal<User | null> = this.loginService.currentUser;
  constructor() {
        effect(() => {
            console.log("Güncel Kullanıcı",this.user());
        })
  }
  userLoginForm : FormGroup = new FormGroup({
      id: new FormControl(""),
      username : new FormControl("",[Validators.required]),
      password : new FormControl("",[Validators.required])
  })

  login(){
        const formValue = this.userLoginForm.value;
        this.loginService.login(formValue.username,formValue.password).subscribe();
  }



}
