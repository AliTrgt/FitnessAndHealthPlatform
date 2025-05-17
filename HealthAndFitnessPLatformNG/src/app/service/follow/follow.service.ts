import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Follow } from '../../model/follow';

@Injectable({
  providedIn: 'root'
})
export class FollowService {
  
  private baseURL = "http://localhost:8080/v1/follow";
  constructor(private http:HttpClient) { }


  getAllFollows() :Observable<Follow[]>{
      return this.http.get<Follow[]>(`${this.baseURL}`);
  }

  follow(follow:Follow) : Observable<Follow>{
    return this.http.post<Follow>(`${this.baseURL}`,follow);
  }

  unFollow(follow:Follow) : Observable<void>{
      return this.http.delete<void>(`${this.baseURL}`,{body : follow});
  }

  isFollow(follow:Follow) : Observable<boolean>{
    const params = new HttpParams()
        .set('followerId',follow.followerId.toString())
        .set('followingId',follow.followingId.toString());

      return this.http.get<boolean>(`${this.baseURL}/isFollow`,{params});
  }
  

}
