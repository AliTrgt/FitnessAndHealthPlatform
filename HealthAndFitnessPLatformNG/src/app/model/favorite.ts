export class Favorite {
     
               id?:number;
               userId:number;
               recipeId:number;
               createdAt?:string;

               constructor(id:number,userId:number,recipeId:number,createdAt:string){
                         this.id = id;
                         this.userId = userId;
                         this.recipeId = recipeId;
                         this.createdAt = createdAt;
               }
}