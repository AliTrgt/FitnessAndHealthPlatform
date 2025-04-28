import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService {
  private baseUrl = "http://localhost:8080/v1";
  constructor(private http:HttpClient) { }


  uploadUserProfilePhoto(userId:number,file:File) : Observable<any> {
        const formData = new FormData();
        formData.append('file',file);

        return this.http.post<any>(`${this.baseUrl}/user/upload/${userId}`,formData,{ responseType: 'text' as 'json' });
  }


  uploadRecipeImagePhoto(recipeId:number,file:File) : Observable<string> {
      const formData = new FormData();
      formData.append('file',file);
      return this.http.post<string>(`${this.baseUrl}/recipe/upload/${recipeId}`,formData,{ responseType: 'text' as 'json' });
  }



}
