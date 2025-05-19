import { Component, effect, inject, Signal, signal } from '@angular/core';
import { AuthUserService } from '../../service/security/auth-user.service';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from "../sidebar/sidebar.component";
import { User } from '../../model/user';
import { Recipe } from '../../model/recipe';
import { RecipeService } from '../../service/recipe/recipe.service';
import { RouterLink, RouterLinkWithHref } from '@angular/router';
import { response } from 'express';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { RecipeSearchPipe } from '../pipe/recipe-search.pipe';
import { FormsModule } from '@angular/forms';



@Component({
  selector: 'app-my-recipes',
  standalone: true,
  imports: [CommonModule, SidebarComponent,RouterLink,ScrollingModule,RecipeSearchPipe,FormsModule],
  templateUrl: './my-recipes.component.html',
  styleUrl: './my-recipes.component.css'
})
export class MyRecipesComponent {
  private authUserService = inject(AuthUserService)  
  searchText = signal('');
  user!:User;
  recipeList = signal<Recipe[] | null>(null);
    constructor(private recipeService:RecipeService){
          const stringToken = localStorage.getItem('currentUser');
          if(stringToken){
              this.user = JSON.parse(stringToken);
              this.getRecipesByUserId(this.user.id);
            }
    }

    isLoggedIn() : boolean{
        return this.authUserService.isLoggedIn();
    }

    getRecipesByUserId(userId:number){
        this.recipeService.getRecipeByUserId(userId).subscribe(response => {
              this.recipeList.set(response);
              
        })
    }
    trackByRecipeId(index: number, recipe: any): number {
      return recipe.id; 
    }

    removeRecipe(id:number){
          this.recipeService.deleteRecipe(id).subscribe({
            next: () => {
                  this.recipeList.update(prev => prev!.filter(recipe => recipe.id !== id));     
            }
          });
    }
}
