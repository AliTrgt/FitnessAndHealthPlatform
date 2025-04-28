import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { User } from '../../model/user';
import { error } from 'console';


@Injectable({
  providedIn: 'root'
})
export class AuthUserService {
  public currentUser = signal<User | null>(null);
  private baseURL = "http://localhost:8080/v1/user";

  constructor(private http:HttpClient,private router:Router) { 
  }

  login(username:string,password:string) : Observable<{accessToken : string}>{
      return this.http.post<{accessToken : string}>(`${this.baseURL}/login`,{username,password}).pipe(
        tap(response => {
            this.saveToken(response.accessToken);
            this.fetchCurrentUser(response.accessToken).subscribe();
            this.router.navigate(["/homepage"]);
        })
      )
      
  }

  private fetchCurrentUser(token:string) : Observable<User>{
    const headers = new HttpHeaders({'Authorization': `Bearer ${token}`})
      
    return this.http.get<User>(`${this.baseURL}/me`,{headers}).pipe(
          tap(user => {
              this.currentUser.set(user);
              localStorage.setItem('currentUser',JSON.stringify(user));
          }),
          catchError(error => {
            this.logOut();
            return throwError(() => error);
          })
      )
  }

  saveToken(accessToken:string){
      localStorage.setItem('accessToken',accessToken);
  }

  getToken() : string{
     return localStorage.getItem('accessToken') as string;
  }

  isLoggedIn() : boolean{
      return !!this.getToken(); 
  }

  logOut(){
      if(localStorage.getItem('accessToken')){
          localStorage.removeItem('accessToken');
          localStorage.removeItem('currentUser');
          localStorage.removeItem('likedRecipes');
          this.currentUser.set(null);
          this.router.navigate(['/homepage']);
      }
  }

}
