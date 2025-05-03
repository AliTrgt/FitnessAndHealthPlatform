import { Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { RecommendComponent } from './components/recommend/recommend.component';
import { FavoritesComponent } from './components/favorites/favorites.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { RecipeComponent } from './components/recipe/recipe.component';
import { AuthGuardService } from './service/security/auth-guard.service';
import { MyRecipesComponent } from './components/my-recipes/my-recipes.component';
import { TrainingComponent } from './components/training/training.component';
import { CreateRecipeComponent } from './components/recipe/create-recipe/create-recipe.component';
import { createTracing } from 'trace_events';
import { CreateTrainingComponent } from './components/training/create-training/create-training.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { FollowersFollowingComponent } from './components/followers-following/followers-following.component';

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
