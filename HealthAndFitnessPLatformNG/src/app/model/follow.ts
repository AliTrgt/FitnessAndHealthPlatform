export class Follow {

     id?:number;
     followerId:number;
     followingId:number;
     createdAt?:string;
     constructor(id:number,followerId:number,followingId:number,createdAt:string){
               this.id = id;
               this.followerId = followerId;
               this.followingId = followingId;
               this.createdAt = createdAt;
     }

}