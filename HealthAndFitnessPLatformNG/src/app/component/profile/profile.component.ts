import { Component, effect, inject, OnInit, Signal, signal } from '@angular/core';
import { AuthUserService } from '../../service/security/auth-user.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { User } from '../../model/user';
import { workoutPlan } from '../../model/workoutPlan';
import { WorkoutPlanService } from '../../service/workoutPlan/workout-plan.service';
import { FormsModule, NgModel } from '@angular/forms';
import { UserService } from '../../service/user/user.service';
import { response } from 'express';
import { ImageUploadService } from '../../service/imageUpload/image-upload.service';
import { FavoriteService } from '../../service/favorite/favorite.service';
import { Favorite } from '../../model/favorite';
import { RecipeService } from '../../service/recipe/recipe.service';
import { Recipe } from '../../model/recipe';
import { LikeService } from '../../service/like/like.service';
import { Like } from '../../model/like';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule,RouterLink,FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  currentBmi: number = 0;
  showAll:boolean = false;
  showFavoriteAll:boolean = false;
  user!:User;
  isEditing:boolean = false;
  selectedFile!:File;
  favorites:Favorite[] = [];
  recipeList:Recipe[] = [];
  likeList:Like[] = [];
  recipeLikeList:Recipe[] = [];
  constructor(private authService:AuthUserService,
    private workoutService:WorkoutPlanService,
    private userService:UserService
    ,private recipeService:RecipeService
    ,private favoriteService:FavoriteService,
     private likeService:LikeService
){}
  
  ngOnInit() {
    const stringToken = localStorage.getItem('currentUser');
    if(stringToken){
          this.user = JSON.parse(stringToken);
    } 
      this.getUserWorkoutPlanByUserId();
      this.getFavoriteRecipes();
      this.getLikedRecipes();
  }

  logOut(){
       this.authService.logOut();
  }

  getFormattedTime() {
    const date = new Date(0, 0, 0, 0, this.totalWorkoutDuration);
    const hours = date.getHours();
    const minutes = date.getMinutes();
    
    if (hours === 0) {
      return `${minutes} dk`;
    }
    if(hours === 1 && minutes === 0){
        return `${hours} saat `;
    }
    else {
      return `${hours} saat ${minutes} dk`;
    }
  }

  getUserWorkoutPlanByUserId(){
      this.workoutService.getWorkoutPlanByUserId(this.user.id).subscribe(response => {
            this.user.workoutPlans = response;
            this.calculateAndUpdateBmi();
      })
  }


  get totalWorkoutDuration() : number {
     return this.user.workoutPlans.reduce((sum,w) => sum + w.duration,0 || 0);
  }

  saveUser(){
      this.userService.updateUser(this.user.id,this.user).subscribe(updatedUser => {
          console.log(updatedUser);
          this.user = updatedUser;  
          localStorage.setItem('currentUser',JSON.stringify(updatedUser));
          this.isEditing = false;
          this.calculateAndUpdateBmi(); 
      })
  }

  calculateAndUpdateBmi() {
    const heightInMeters = this.user.height / 100;
    const score = this.user.weight / (heightInMeters * heightInMeters);
    this.currentBmi = score;

    this.userService.changeBMIValue(this.user.id, score).subscribe({
      next: () => console.log("BMI güncellendi"),
      error: (err) => console.error("BMI güncellenirken hata:", err)
    });
  }

  getFavoriteRecipes(){
      this.favoriteService.findFavoritesByUserId(this.user.id).subscribe(response => {
          this.favorites = response;
          const recipesId: number[] = this.favorites.map(recipe => recipe.recipeId);
          
          recipesId.map(recipeId => {
            this.recipeService.findById(recipeId).subscribe(response => {
              this.recipeList.push(response);
        })
          })
      })

  }


  getLikedRecipes(){
      this.likeService.getLikesByUserId(this.user.id).subscribe(response => {
        console.log(this.likeList);  
        this.likeList = response;
          const recipeIds = this.likeList.map(x => x.recipeId);


          recipeIds.forEach(recipeId => {
                  this.recipeService.findById(recipeId).subscribe(response => {
                        this.recipeLikeList.push(response);
                  })
          })

      })
  }

  toggleShowAll(){
      this.showAll = !this.showAll;
  }

  toggleFavoritedShowAll(){
      this.showFavoriteAll = !this.showFavoriteAll;
  }

}
