import { ChangeDetectionStrategy, ChangeDetectorRef, Component, effect, inject, numberAttribute, OnInit, Output, Signal, signal } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { User } from '../../model/user';
import { RecipeService } from '../../service/recipe/recipe.service';
import { Recipe } from '../../model/recipe';
import { CommonModule, } from '@angular/common';
import { NavbarComponent } from "../navbar/navbar.component";
import { LoginComponent } from "../login/login.component";
import { RouterLink } from '@angular/router';
import { AuthUserService } from '../../service/security/auth-user.service';
import { response } from 'express';
import { error } from 'console';
import { UserService } from '../../service/user/user.service';
import { DateAgoPipe } from '../pipe/date-ago.pipe';
import { SidebarComponent } from "../sidebar/sidebar.component";
import { LikeService } from '../../service/like/like.service';
import { Like } from '../../model/like';
import { resolveAny } from 'dns';
import { getRandomValues, sign } from 'crypto';
import { forkJoin, Observable, of } from 'rxjs';


@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, RouterLink, DateAgoPipe, SidebarComponent],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent implements OnInit {
  public recipeService = inject(RecipeService);
  recipes = signal<Recipe[] | null>(null);
  trendRecipes: Recipe[] = [];
  recipeQuantity!: number;
  usernameList: string[] = [];
  currentUser!: User;
  likedRecipes: Set<number> = new Set();
  userMap =  new Map<number,User>;
  constructor(private authUserService: AuthUserService, private userService: UserService, private likeService: LikeService) { }
  ngOnInit() {
    const userToken = localStorage.getItem('currentUser');
    if (userToken) {
      this.currentUser = JSON.parse(userToken);
      this.loadUserLikesFromDB(this.currentUser.id);
    }
    this.loadLikesFromLocalStorage(); 
    this.getAllRecipes();
  }

  loadLikesFromLocalStorage() {
    const storedLikes = localStorage.getItem('likedRecipes');
    if (storedLikes) {
      const likedIds: number[] = JSON.parse(storedLikes);
      this.likedRecipes = new Set(likedIds);
    }
  }

  isLoggedIn(): boolean {
    return this.authUserService.isLoggedIn();
  }

  getAllRecipes() {
    this.recipeService.getAllRecipes().subscribe(recipe => {
      this.recipes.set(recipe);
  
      // User bilgilerini almak için API çağrıları
      const userObservables = recipe.map(r =>
        this.userService.findById(r.userId)  // recipe.userId ile kullanıcıyı bul
      );
  
      forkJoin(userObservables).subscribe(users => {
        // Kullanıcıları userMap'e ekle
        users.forEach(user => {
          this.userMap.set(user.id, user);
          
        });
        
        // Artık her recipe'yi userMap ile eşleştirebilirsiniz
        this.usernameList = recipe.map(r => this.userMap.get(r.userId)?.username || 'Unknown');
        // Ayrıca fotoğraf url'leri de burada kullanabilirsiniz
      });
    });
    
  }
  
  toggleLikeRecipe(recipeId: number) {
    const like: Like = { userId: this.currentUser.id, recipeId: recipeId }
    this.likeService.toggleLike(like).subscribe(response => {
        const recipe =   this.recipes()!.find(r => r.id === recipeId);
          if(!recipe) return;

          const existingLikeIndex = recipe.likeList.findIndex(l => l.userId ===this.currentUser.id);

          if(existingLikeIndex !== -1 ){
              recipe.likeList.splice(existingLikeIndex,1);
              recipe.likeCount--;
              this.likedRecipes.delete(recipeId);
          } else {
                recipe.likeList.push(like);
                recipe.likeCount++;
                this.likedRecipes.add(recipeId);
          }

          this.saveLikesToLocalStorage();
    });
  }

   likedRecipeHas(recipeId:number) : boolean {
     return this.likedRecipes.has(recipeId);  
    }

  saveLikesToLocalStorage() {
    localStorage.setItem('likedRecipes', JSON.stringify([...this.likedRecipes]));
  }

  loadUserLikesFromDB(userId: number) {
    this.likeService.getLikesByUserId(userId).subscribe(likes => {
      const likedIds = likes.map(like => like.recipeId);
      this.likedRecipes = new Set(likedIds);
      this.saveLikesToLocalStorage(); // Eğer local'de tutmak istiyorsan
    });
  }


}
