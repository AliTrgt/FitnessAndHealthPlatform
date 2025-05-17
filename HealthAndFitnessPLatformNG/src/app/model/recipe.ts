import { Comment } from "./comment";
import { Ingredient } from "./ingredient";
import { Like } from "./like";

export class Recipe {
     id : number;
     title : string;
     description : string;
     instructions:string;
     prepTime : number;
     calories : number;
     createdAt : string;
     likeCount : number;
     imageUrl:string;
     userId : number;
     ingredientList : Ingredient[];
     likeList : Like[];
     commentList : Comment[];

     constructor(id:number,title:string,description:string,instructions:string,prepTime:number,calories:number,createdAt:string,likeCount:number,imageUrl:string,userId:number,ingredientList:Ingredient[],likeList:Like[],commentList:Comment[]){
               this.id = id;
               this.title = title;
               this.description = description;
               this.instructions = instructions;
               this.prepTime = prepTime;
               this.calories = calories;
               this.createdAt = createdAt;
               this.likeCount = likeCount;
               this.imageUrl = imageUrl;
               this.userId = userId;
               this.ingredientList = ingredientList;
               this.likeList = likeList;
               this.commentList = commentList;
     }

}