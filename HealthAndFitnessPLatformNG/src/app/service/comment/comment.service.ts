import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private baseUrl = "http://localhost:8080/v1/comment";
  constructor(private http:HttpClient) { }

  getAllComments() : Observable<Comment[]>{
      return this.http.get<Comment[]>(`${this.baseUrl}`);
  }

  findById(commentId:number) : Observable<Comment>{
      return this.http.get<Comment>(`${this.baseUrl}/${commentId}`);
  }

  createComment(comment:Comment) : Observable<Comment>{
      return this.http.post<Comment>(`${this.baseUrl}/create`,comment);
  }

  updateComment(commentId:number,comment:Comment) : Observable<Comment>{
      return this.http.put<Comment>(`${this.baseUrl}/update/${commentId}`,comment);
  }

  deleteComment(commentId:number) : Observable<void>{
      return this.http.delete<void>(`${this.baseUrl}/${commentId}`);
  }

}
