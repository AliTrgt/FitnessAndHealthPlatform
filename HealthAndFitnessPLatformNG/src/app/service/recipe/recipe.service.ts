import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, signal, Signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Recipe } from '../../model/recipe';
import { response } from 'express';
import { sign } from 'crypto';
import { User } from '../../model/user';
import { PageResponse } from '../../model/pageresponse';
@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private baseURL = "http://localhost:8080/v1/recipe";
  constructor(private http:HttpClient) { }

  getAllRecipes() : Observable<Recipe[]>{
      return this.http.get<Recipe[]>(`${this.baseURL}`);
  }

  getRecipes(page:number = 0,size:number = 10) : Observable<PageResponse<Recipe>>{
        const params = new HttpParams()
        .set('page',page)
        .set('size',size)
        return this.http.get<PageResponse<Recipe>>(`${this.baseURL}/get10`,{ params })
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
