import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { User } from '../../model/user';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseURL = "http://localhost:8080/v1/user";
  constructor(private http:HttpClient,private router:Router) {}

  getAllUsers() : Observable<User[]>{
      return this.http.get<User[]>(`${this.baseURL}`);
  }

  findById(userId:number) : Observable<User>{
      return this.http.get<User>(`${this.baseURL}/${userId}`);
  }

  createUser(user:User) : Observable<User>{
      return this.http.post<User>(`${this.baseURL}/create`,user);
  }

  updateUser(userId:number,user:User) : Observable<User>{
      return this.http.put<User>(`${this.baseURL}/update/${userId}`,user)
  }

  deleteUser(userId:number) : Observable<void>{
      return this.http.delete<void>(`${this.baseURL}/${userId}`);
  }

  changeBMIValue(userId:number,newValue:number) : Observable<void>{
        return this.http.post<void>(`${this.baseURL}/change/${userId}`,{ newValue });
  }
  

}
