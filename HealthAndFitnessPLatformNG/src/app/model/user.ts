import { Favorite } from "./favorite";
import { Follow } from "./follow";
import { Like } from "./like";
import { Recipe } from "./recipe";
import { Role } from "./role";
import { Comment } from "./comment";
import { workoutPlan } from "./workoutPlan";

export class User {
     id : number;
     username : string;
     password : string;
     authorities : Role[];
     email : string;
     workoutPlans:workoutPlan[];
     profilePhoto : string;
     height : number;
     weight : number;
     gender:string;
     age:number;
     bmi:number;
     createdAt : string;
     recipeList : Recipe[];
     likeList : Like[];
     commentList : Comment[];
     favoriteList : Favorite[];
     followers : Follow[];
     following : Follow[];

     constructor(id:number,username:string,password:string,authorities:Role[],email:string,workoutPlans:workoutPlan[],profilePhoto:string,height:number,weight:number,gender:string,age:number,bmi:number,createdAt:string,recipeList:Recipe[],likeList:Like[],commentList:Comment[],favoriteList:Favorite[],followers:Follow[],following:Follow[]){
          this.id = id;
          this.username = username;
          this.password = password;
          this.authorities = authorities;
          this.email = email;
          this.workoutPlans = workoutPlans;
          this.profilePhoto = profilePhoto;
          this.height =height;
          this.weight = weight;
          this.gender = gender;
          this.age = age;
          this.bmi = bmi;
          this.createdAt = createdAt;
          this.recipeList =recipeList;
          this.likeList = likeList;
          this.commentList = commentList;
          this.favoriteList = favoriteList;
          this.followers = followers;
          this.following = following;
     }



}