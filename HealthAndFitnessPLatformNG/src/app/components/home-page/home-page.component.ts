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
import { ReversePipe } from '../pipe/reverse.pipe';
import { ScrollingModule } from '@angular/cdk/scrolling';


@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, RouterLink, DateAgoPipe, SidebarComponent, ScrollingModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent implements OnInit {
  public recipeService = inject(RecipeService);
  public authUserService = inject(AuthUserService);
  recipes = signal<Recipe[] | null>(null);
  trendRecipes: Recipe[] = [];
  recipeQuantity!: number;
  usernameList: string[] = [];
  currentUser!: User;
  likedRecipes: Set<number> = new Set();
  userMap =  new Map<number,User>;

  currentPage: number = 0;
  pageSize: number = 10;
  constructor(private userService: UserService, private likeService: LikeService) { }
  
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

  getAllRecipes(page: number = 0, size: number = 10): void {
    this.recipeService.getRecipes(page, size).subscribe(response => {
      const recipeList = response.content;
      this.recipes.set(recipeList);
      this.recipeQuantity = response.totalElements;
  
      // Tüm userId'leri benzersiz olarak al
      const uniqueUserIds = [...new Set(recipeList.map(r => r.userId))];
  
      // Toplu olarak kullanıcıları çek
      this.userService.findAllByIds(uniqueUserIds).subscribe(users => {
        users.forEach(user => {
          this.userMap.set(user.id, user);
        });
      });
    });
  }
  

  get reversedRecipes(): Recipe[] {
    const recipes = this.recipes();
    return recipes ? [...recipes].reverse() : [];
  }

  trackById(index: number, item: Recipe) {
    return item.id;
  }

  loadNextPage() {
    this.currentPage++;
    this.getAllRecipes(this.currentPage, this.pageSize);
  }

  loadPreviousPage(){
    this.currentPage--;
    this.getAllRecipes(this.currentPage, this.pageSize);
  }

  
  toggleLikeRecipe(recipeId: number) {
    if (!this.currentUser || !this.currentUser.id) {
      console.warn('Kullanıcı bilgisi henüz yüklenmedi. Like atılamaz.');
      return;
    }
  
    const like: Like = { userId: this.currentUser.id, recipeId: recipeId };
    
    this.likeService.toggleLike(like).subscribe({
      next: response => {
        const recipe = this.recipes()!.find(r => r.id === recipeId);
        if (!recipe) return;
  
        const existingLikeIndex = recipe.likeList.findIndex(l => l.userId === this.currentUser.id);
        if (existingLikeIndex !== -1) {
          recipe.likeList.splice(existingLikeIndex, 1);
          recipe.likeCount--;
          this.likedRecipes.delete(recipeId);
        } else {
          recipe.likeList.push(like);
          recipe.likeCount++;
          this.likedRecipes.add(recipeId);
        }
  
        this.saveLikesToLocalStorage();
      },
      error: err => {
        console.error('Like API hatası:', err);
      }
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
