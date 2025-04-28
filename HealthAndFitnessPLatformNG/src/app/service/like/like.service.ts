import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Like } from '../../model/like';

@Injectable({
  providedIn: 'root'
})
export class LikeService {

  private baseURL = "http://localhost:8080/v1/like"
  constructor(private http:HttpClient) { }


  getAllLikes() : Observable<Like[]>{
      return this.http.get<Like[]>(`${this.baseURL}`);
  }

  like(like:Like) : Observable<Like>{
      return this.http.post<Like>(`${this.baseURL}`,like);
  }

  dislike(like:Like) : Observable<void>{
      return this.http.delete<void>(`${this.baseURL}/dislike`,{body : like});
  }
  
  toggleLike(like:Like) : Observable<Like>{
      return this.http.post<Like>(`${this.baseURL}/toggleLike`,like);
  }

  findLikeByUserId(userId:number) : Observable<Like>{
      return this.http.get<Like>(`${this.baseURL}/user/${userId}`);
  }  

  getLikesByUserId(userId: number) : Observable<Like[]>{
        return this.http.get<Like[]>(`${this.baseURL}/getLikesById/${userId}`);
  }

}
