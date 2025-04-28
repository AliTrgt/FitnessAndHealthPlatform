import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { workoutPlan } from '../../model/workoutPlan';

@Injectable({
  providedIn: 'root'
})
export class WorkoutPlanService {
  private baseUrl = "http://localhost:8080/workout"
  constructor(private http:HttpClient) { }

  createWorkoutPlan(wplan:workoutPlan) : Observable<workoutPlan>{
      return  this.http.post<workoutPlan>(`${this.baseUrl}/create`,wplan);
  }

  deleteWorkoutPlan(id:number) : Observable<void>{
      return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getWorkoutPlanByUserId(userId:number) :Observable<workoutPlan[]>{
      return this.http.get<workoutPlan[]>(`${this.baseUrl}/${userId}`);
  }

}
