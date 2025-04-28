export class Like {

     likeId ?: number;
     userId : number;
     recipeId : number;
     createdAt ?: string;

     constructor(likeId:number,userId:number,recipeId:number,createdAt:string){
               this.likeId = likeId;
               this.userId =userId;
               this.recipeId = recipeId;
               this.createdAt = createdAt;
     }

}