import { Routes } from '@angular/router';
import { HomePageComponent } from './component/home-page/home-page.component';
import { RecommendComponent } from './component/recommend/recommend.component';
import { FavoritesComponent } from './component/favorites/favorites.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { ProfileComponent } from './component/profile/profile.component';
import { RecipeComponent } from './component/recipe/recipe.component';
import { AuthGuardService } from './service/security/auth-guard.service';
import { MyRecipesComponent } from './component/my-recipes/my-recipes.component';
import { TrainingComponent } from './component/training/training.component';
import { CreateRecipeComponent } from './component/recipe/create-recipe/create-recipe.component';
import { createTracing } from 'trace_events';
import { CreateTrainingComponent } from './component/training/create-training/create-training.component';
import { UserProfileComponent } from './component/user-profile/user-profile.component';
import { FollowersFollowingComponent } from './component/followers-following/followers-following.component';

export const routes: Routes = [
     {path:"",component:HomePageComponent,pathMatch:"full"},
     {path:"homepage",component:HomePageComponent},
     {path:"recommend",component:RecommendComponent,canActivate:[AuthGuardService]},
     {path:"favorites",component:FavoritesComponent,canActivate:[AuthGuardService]},
     {path:"login",component:LoginComponent},
     {path:"register",component:RegisterComponent},
     {path:"profile",component:ProfileComponent,canActivate:[AuthGuardService]},
     {path:"userprofile/:id",component:UserProfileComponent},
     {path:"recipe/:id",component:RecipeComponent},
     {path:"myrecipes",component:MyRecipesComponent,canActivate:[AuthGuardService]},
     {path:"training",component:TrainingComponent},
     {path:"createRecipe",component:CreateRecipeComponent},
     {path:"recipe/edit/:id",component:CreateRecipeComponent},
     {path:"createTraining",component:CreateTrainingComponent},
     {path:"followers",component:FollowersFollowingComponent}
];
