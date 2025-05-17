export class Comment {
          
          id:number;
          userId:number;
          recipeId:number;
          content!:string;
          createdAt:string;

          constructor(id:number,userId:number,recipeId:number,content:string,createdAt:string){
                    
                    this.id = id;
                    this.userId = userId;
                    this.recipeId = recipeId;
                    this.content = content;
                    this.createdAt = createdAt;
          }

}