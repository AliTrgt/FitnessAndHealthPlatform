import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../../model/user';
import { CommonModule } from '@angular/common';
import { WorkoutPlanService } from '../../../service/workoutPlan/workout-plan.service';
import { Route, RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { workoutPlan } from '../../../model/workoutPlan';

@Component({
  selector: 'app-create-training',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './create-training.component.html',
  styleUrl: './create-training.component.css'
})
export class CreateTrainingComponent implements OnInit {
  workoutPlan:workoutPlan[] = [];
  createForm!:FormGroup;
  user!:User;
  constructor(private fb:FormBuilder,private workoutPlanService:WorkoutPlanService,private router:Router,private workoutPlans:WorkoutPlanService){

  }
  ngOnInit() {
        const userToken = localStorage.getItem('currentUser');
        userToken ? this.user = JSON.parse(userToken) : null ;

        this.createForm = this.fb.group({
              id : [''],
              workoutType : ['',Validators.required],
              duration : [''],
              burningCalories :[''],
              workoutMessage : ['',Validators.required],
              userId : [''],
              createdAt : ['',Validators.required]
        })
        this.getWorkoutPlanByUserId();
      }


      getWorkoutPlanByUserId(){
        this.workoutPlans.getWorkoutPlanByUserId(this.user.id).subscribe((response) => {
            this.workoutPlan = response;
            console.log(response);
        })
    }


      createWorkoutPlan(){
          const formData = {...this.createForm.value,userId:this.user.id,}
          this.workoutPlanService.createWorkoutPlan(formData).subscribe(response => {
                    console.log(response);
                    this.router.navigate(['/training']);
          })
      }




  


}
