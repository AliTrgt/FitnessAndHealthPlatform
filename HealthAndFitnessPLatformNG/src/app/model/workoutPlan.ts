export class workoutPlan{

          id:number;
          workoutType:string;
          duration:number;
          burningCalories:number;
          workoutMessage:string;
          userId:number;
          createdAt : string;

          constructor(id:number,workoutType:string,duration:number,burningCalories:number,workoutMessage:string,userId:number,createdAt:string){
                         this.id = id;
                         this.workoutType = workoutType;
                         this.duration = duration;
                         this.burningCalories = burningCalories;
                         this.workoutMessage = workoutMessage;
                         this.userId = userId;
                         this.createdAt = createdAt;
          }

}