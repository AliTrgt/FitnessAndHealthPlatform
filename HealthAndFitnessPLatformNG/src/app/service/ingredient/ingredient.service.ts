import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ingredient } from '../../model/ingredient';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {

  private baseURL = "http://localhost:8080/v1/ingredient"
  constructor(private http:HttpClient) { }


  getAllIngredients() : Observable<Ingredient[]>{
      return this.http.get<Ingredient[]>(`${this.baseURL}`);
  }

  findById(ingredientId:number) :Observable<Ingredient>{
      return this.http.get<Ingredient>(`${this.baseURL}/${ingredientId}`);
  }

  createIngredient(recipeId:number,ingredient:Ingredient) : Observable<Ingredient>{
      return this.http.post<Ingredient>(`${this.baseURL}/create/${recipeId}`,ingredient);
  }

  updateIngredient(ingredientId:number,ingredient:Ingredient) : Observable<Ingredient>{
      return this.http.put<Ingredient>(`${this.baseURL}/update/${ingredientId}`,ingredient);
  }

  deleteIngredient(ingredientId:number) : Observable<void>{
      return this.http.delete<void>(`${this.baseURL}/${ingredientId}`);
  }

}
