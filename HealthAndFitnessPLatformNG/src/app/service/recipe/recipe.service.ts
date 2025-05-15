import { HttpClient } from '@angular/common/http';
import { Injectable, signal, Signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Recipe } from '../../model/recipe';
import { response } from 'express';
import { sign } from 'crypto';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private baseURL = "http://localhost:8080/v1/recipe";
  constructor(private http:HttpClient) { }

  getAllRecipes() : Observable<Recipe[]>{
      return this.http.get<Recipe[]>(`${this.baseURL}`);
  }

  getRecipes(page:number = 0){
        return this.http.get<any>(`${this.baseURL}/recipes?page=${page}`)
  }

  findById(recipeId:number) : Observable<Recipe>{
      return this.http.get<Recipe>(`${this.baseURL}/${recipeId}`);
  }

  getRecipeByUserId(userId:number) : Observable<Recipe[]>{
      return this.http.get<Recipe[]>(`${this.baseURL}/find/${userId}`);
  }

  getRecommendation(recipeId:number) :Observable<Recipe[]> { 
      return this.http.get<Recipe[]>(`${this.baseURL}/rec/${recipeId}`);
  }

  createRecipe(recipe:Recipe) : Observable<Recipe>{
      return this.http.post<Recipe>(`${this.baseURL}/create`,recipe);
  }

  updateRecipe(recipeId:number,recipe:Recipe) : Observable<Recipe>{
      return this.http.put<Recipe>(`${this.baseURL}/update/${recipeId}`,recipe);
  }

  deleteRecipe(recipeId:number) : Observable<void>{
      return this.http.delete<void>(`${this.baseURL}/${recipeId}`);
  }
}
