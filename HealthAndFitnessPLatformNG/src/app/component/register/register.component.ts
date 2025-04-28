import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UserService } from '../../service/user/user.service';
import { Router } from "@angular/router";
import { User } from '../../model/user';
import { promiseHooks } from 'node:v8';
import { ImageUploadService } from '../../service/imageUpload/image-upload.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink,ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  selectedFile!:File;
  constructor(private userService:UserService,private router:Router,private imageUploadService:ImageUploadService){}
  userRegisterForm : FormGroup = new FormGroup({
      id : new FormControl(""),
      username : new FormControl("",[Validators.required]),
      password : new FormControl("",Validators.required),
      email : new FormControl("",Validators.email),
      authorities : new FormControl("",Validators.required),
      height : new FormControl("",Validators.required),
      weight : new FormControl("",Validators.required),
      gender : new FormControl("",[Validators.required]),
      age : new FormControl("",[Validators.required])
  })


  onPhotoSelected(event: any){
        const file = event.target.files[0];
        if(file){
            this.selectedFile = file;
        }
  }

  register(){
      const form = this.userRegisterForm.value;
      form.authorities = [form.authorities];

      this.userService.createUser(form).subscribe(createdUser =>{

        if(this.selectedFile){
              this.imageUploadService.uploadUserProfilePhoto(createdUser.id,this.selectedFile).subscribe({
                next :  () =>{ 
                  createdUser.profilePhoto = this.selectedFile.name;
                  this.router.navigate(["/login"])
                },
                error: () => console.error('Fotoğraf yükleme hatası:')
              });
                this.router.navigate(['/login']);
        }

      })
  }
   

}
