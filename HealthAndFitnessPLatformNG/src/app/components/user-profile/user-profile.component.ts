import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../../model/user';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UserService } from '../../service/user/user.service';
import { response } from 'express';
import { RecipeService } from '../../service/recipe/recipe.service';
import { Recipe } from '../../model/recipe';
import { Like } from '../../model/like';
import { LikeService } from '../../service/like/like.service';
import { FollowService } from '../../service/follow/follow.service';
import { Follow } from '../../model/follow';
import { ScrollingModule } from '@angular/cdk/scrolling';



@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule,FormsModule,RouterLink,ScrollingModule],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  user!:User;
  sessionUser!:User;
  userId!:number;
  recipes:Recipe[] = [];
  isFollowing:boolean = false;
  likedRecipes: Set<number> = new Set();
  constructor(private router:ActivatedRoute,private userService:UserService,private recipeService:RecipeService,private likeService:LikeService,private followService:FollowService){}
  
  ngOnInit() {
    const sessionUser = localStorage.getItem('currentUser');
    if(sessionUser){
          this.sessionUser = JSON.parse(sessionUser);
          this.loadUserLikesFromDB(this.sessionUser.id);
    }
    this.loadLikesFromLocalStorages();
        this.router.paramMap.subscribe(req => {
            this.userId = Number(req.get('id'));
        })
        this.findById();
        this.getAllRecipes();
        this.checkFollowStatus();
  }

  toggleLikeRecipe(recipeId: number) {
    const like: Like = { userId: this.sessionUser.id , recipeId: recipeId };
    this.likeService.toggleLike(like).subscribe(response => {
      const recipe = this.recipes.find(r => r.id === recipeId);
      if (!recipe) return;
  
      const existingLikeIndex = recipe.likeList.findIndex(l => l.userId === this.sessionUser.id);
  
      if (existingLikeIndex !== -1) {
        recipe.likeList.splice(existingLikeIndex, 1);
        recipe.likeCount--;
        this.likedRecipes.delete(recipeId);
      } else {
        recipe.likeList.push(like);
        recipe.likeCount++;
        this.likedRecipes.add(recipeId);
      }
  
      this.savesLikesToLocalStorages();
    });
  }


  
  
  toggleFollowUser(){
    if(this.isFollowing){
      const follow : Follow = {followerId : this.userId , followingId :this.sessionUser.id}
      this.followService.unFollow(follow).subscribe(response => {
          console.log(response);
          this.isFollowing = false;
            this.userService.findById(this.userId).subscribe(response => {
                  this.user = response;
            })
      })
    } else {
      const follow : Follow = {followerId : this.userId , followingId :this.sessionUser.id}
        this.followService.follow(follow).subscribe(response => {
          console.log(response);
          this.isFollowing = true;
            this.userService.findById(this.userId).subscribe(response => {
                  this.user = response;
            })
        })

    }   
  }

  checkFollowStatus(){
      const follow:Follow = {followerId: this.userId,followingId: this.sessionUser.id}
      this.followService.isFollow(follow).subscribe(response => {
          this.isFollowing = response;
      })
  }


  findById(){
      this.userService.findById(this.userId).subscribe(response => {
            this.user = response;
      })
  }


  getAllRecipes(){
      this.recipeService.getRecipeByUserId(this.userId).subscribe(response => {
          this.recipes = response;
      })
  }

  loadLikesFromLocalStorages(){
        const storedLike = localStorage.getItem('likedRecipes');
        if(storedLike){
              const likeIds : number[] = JSON.parse(storedLike);
              this.likedRecipes = new Set(likeIds);
        }
  }

  savesLikesToLocalStorages(){
      localStorage.setItem('likedRecipes',JSON.stringify([...this.likedRecipes]));
  }

  likedRecipeHas(recipeId : number) : boolean {
      return this.likedRecipes.has(recipeId);
  }

  loadUserLikesFromDB(userId: number) {
    this.likeService.getLikesByUserId(userId).subscribe(likes => {
      const likedIds = likes.map(like => like.recipeId);
      this.likedRecipes = new Set(likedIds);
      this.savesLikesToLocalStorages(); // Eğer local'de tutmak istiyorsan
    });
  }



}
