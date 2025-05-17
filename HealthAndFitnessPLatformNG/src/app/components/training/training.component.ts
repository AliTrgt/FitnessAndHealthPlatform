import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthUserService } from '../../service/security/auth-user.service';
import { SidebarComponent } from "../sidebar/sidebar.component";
import { User } from '../../model/user';
import { Route, RouterLink } from '@angular/router';
import { WorkoutPlanService } from '../../service/workoutPlan/workout-plan.service';
import { workoutPlan } from '../../model/workoutPlan';
import { response } from 'express';
import { DateAgoPipe } from '../pipe/date-ago.pipe';

@Component({
  selector: 'app-training',
  standalone: true,
  imports: [CommonModule, SidebarComponent,RouterLink,DateAgoPipe],
  templateUrl: './training.component.html',
  styleUrl: './training.component.css'
})
export class TrainingComponent {
  user!:User;
  workoutPlan:workoutPlan[] = [];

  constructor(private authUserService:AuthUserService,private workoutPlans:WorkoutPlanService){
    const stringToken = localStorage.getItem('currentUser');
    if(stringToken){
          this.user = JSON.parse(stringToken);
    }
    this.getWorkoutPlanByUserId();
  }

  isLoggedIn() : boolean {
      return this.authUserService.isLoggedIn();
  }


  getWorkoutPlanByUserId(){
      this.workoutPlans.getWorkoutPlanByUserId(this.user.id).subscribe((response) => {
          this.workoutPlan = response;
          console.log(response);
      })
  }

  deleteTraining(ID:number){
      this.workoutPlans.deleteWorkoutPlan(ID).subscribe(response => {
            this.workoutPlan = this.workoutPlan.filter(x => x.id !== ID);
      }
      )
  }



  getWorkoutImage(type: string): string {
    switch (type.toLowerCase()) {
      case 'running':
        return 'https://images.unsplash.com/photo-1668188697843-ab5b42e9fbd8?q=80&w=1936&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // person running on a track :contentReference[oaicite:0]{index=0}
      case 'weightlifting':
        return 'https://images.unsplash.com/photo-1741332528292-c4d6bdd8ff1e?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // muscular man weightlifting :contentReference[oaicite:1]{index=1}
      case 'cycling':
        return 'https://images.unsplash.com/photo-1723465739996-d14b6791b2a8?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // group riding bikes on a track :contentReference[oaicite:2]{index=2}
      case 'swimming':
        return 'https://images.unsplash.com/photo-1652096245640-5501b0e99836?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // person swimming in open water :contentReference[oaicite:3]{index=3}
      case 'yoga':
        return 'https://images.unsplash.com/photo-1743630459830-270ef1e7cb38?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // yoga in a studio :contentReference[oaicite:4]{index=4}
      case 'hiit':
        return 'https://images.unsplash.com/photo-1734189605012-f03d97a4d98f?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // kickbox HIIT session :contentReference[oaicite:5]{index=5}
      case 'pilates':
        return 'https://plus.unsplash.com/premium_photo-1663013543267-32c980fec43f?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // pilates plank at home :contentReference[oaicite:6]{index=6}
      case 'walking':
        return 'https://images.unsplash.com/photo-1733667917418-f4b7ea5a80c4?q=80&w=2069&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // forest walk in autumn :contentReference[oaicite:7]{index=7}
      case 'boxing':
        return 'https://images.unsplash.com/photo-1731955138970-fc88fd48b80f?q=80&w=2032&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // woman with boxing gloves :contentReference[oaicite:8]{index=8}
      case 'dancing':
        return 'https://images.unsplash.com/photo-1563775957408-fd3887e986ca?q=80&w=2030&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // stage dancing in Istanbul :contentReference[oaicite:9]{index=9}
      case 'crossfit':
        return 'https://images.unsplash.com/photo-1682530678206-d148e0e2bb0d?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // barbell lift in CrossFit gym :contentReference[oaicite:10]{index=10}
      case 'rowing':
        return 'https://images.unsplash.com/photo-1742316796874-d7f3cb507938?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // early‑morning rowing training :contentReference[oaicite:11]{index=11}
      case 'hiking':
        return 'https://images.unsplash.com/photo-1721241843448-af1c8ade1835?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // mountain trail hike :contentReference[oaicite:12]{index=12}
      case 'jump_rope':
        return 'https://plus.unsplash.com/premium_photo-1672784158416-cdb952e6d767?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // jump‑rope in mid‑air :contentReference[oaicite:13]{index=13}
      default:
        return 'https://images.unsplash.com/photo-1668188697843-ab5b42e9fbd8?q=80&w=1936&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'; // default fitness image :contentReference[oaicite:14]{index=14}
    }
  }
  
  
}
