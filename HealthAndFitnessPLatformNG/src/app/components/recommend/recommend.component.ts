import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RecipeComponent } from '../recipe/recipe.component';
import { RecipeService } from '../../service/recipe/recipe.service';
import { User } from '../../model/user';
import { response } from 'express';
import { Recipe } from '../../model/recipe';
import { RouterLink } from '@angular/router';
import { error } from 'console';

@Component({
  selector: 'app-recommend',
  standalone: true,
  imports: [CommonModule,FormsModule,RouterLink],
  templateUrl: './recommend.component.html',
  styleUrl: './recommend.component.css'
})
export class RecommendComponent implements OnInit {

  user!:User;
  recipeList:Recipe[] = [];
  errorMessage:string = '';
  constructor(private recipeService:RecipeService){}
  ngOnInit() {
        const stringToken = localStorage.getItem('currentUser');
        if(stringToken){
            this.user = JSON.parse(stringToken);
        }
  }

  getReccomendation(){
      this.recipeService.getRecommendation(this.user.id).subscribe(response => {
            this.recipeList = response;
            console.log(response);
            this.errorMessage = '';
      },
    (error)  => {
          console.log('Api error');
          this.errorMessage = 'En az 3-5 post beğenip favorilemelisin.';
    }
  
  )
  }


  


}
