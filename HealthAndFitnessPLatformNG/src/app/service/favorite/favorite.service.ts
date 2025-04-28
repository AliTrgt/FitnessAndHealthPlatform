import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Favorite } from '../../model/favorite';
import { Like } from '../../model/like';
import { Follow } from '../../model/follow';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {

  private baseURL = "http://localhost:8080/v1/favorite";
  constructor(private http:HttpClient) { }

  getAllFavorites() : Observable<Favorite[]>{
      return this.http.get<Favorite[]>(`${this.baseURL}`);
  }

  createFavorite(favorite:Favorite) : Observable<Favorite>{
      return this.http.post<Favorite>(`${this.baseURL}/create`,favorite);
  }

  deleteFavorite(favorite:Favorite) :Observable<void>{
        return this.http.delete<void>(`${this.baseURL}/delete`,{body : favorite});
  }

  toggleFavorite(like:Like) :Observable<Favorite>{
      return this.http.post<Favorite>(`${this.baseURL}/toggle`,like);
  }

  findByUserId(userId:number) : Observable<Favorite>{
      return this.http.get<Favorite>(`${this.baseURL}/${userId}`);
  }

  findFavoritesByUserId(userId : number) : Observable<Favorite[]>{
      return this.http.get<Favorite[]>(`${this.baseURL}/list/${userId}`);
  }

  isExistsByUserIdAndRecipeId(userId:number , recipeId:number) : Observable<boolean>{
    const params = new HttpParams()
    .set('userId',userId)
    .set('recipeId',recipeId)    
    
    return this.http.get<boolean>(`${this.baseURL}/isFavorite`,{params});
  }

}
