import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { sign } from 'crypto';
import { Recipe } from '../../model/recipe';
import { RecipeService } from '../../service/recipe/recipe.service';
import { response } from 'express';
import { CommonModule, DatePipe, formatDate } from '@angular/common';
import { UserService } from '../../service/user/user.service';
import { User } from '../../model/user';
import { truncateSync } from 'fs';
import { get } from 'http';
import { DateAgoPipe } from '../pipe/date-ago.pipe';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommentService } from '../../service/comment/comment.service';
import { Comment } from '../../model/comment';
import { forkJoin } from 'rxjs';
import { Favorite } from '../../model/favorite';
import { FavoriteService } from '../../service/favorite/favorite.service';


@Component({
  selector: 'app-recipe',
  standalone: true,
  imports: [CommonModule, DateAgoPipe, ReactiveFormsModule,RouterLink,FormsModule],
  templateUrl: './recipe.component.html',
  styleUrl: './recipe.component.css',
})
export class RecipeComponent implements OnInit {
  recipe!: Recipe;
  user!: User;
  commentUser!:User;
  recipeId!: number;
  commentList!: Comment[];
  usernameList: string[] = [];
  dateList: string[] = [];
  isOpen: boolean = false;
  isFavorited:boolean = false;
  commentSectionForm: FormGroup = new FormGroup({
    id: new FormControl(""),
    userId: new FormControl(""),
    recipeId: new FormControl(""),
    content: new FormControl("",[Validators.required])
  })
  constructor(private route: ActivatedRoute, private recipeService: RecipeService, private userService: UserService, private commentService: CommentService,private favoriteService:FavoriteService) { }
  ngOnInit() {
    const stringUser = localStorage.getItem('currentUser');
    stringUser ? this.user = JSON.parse(stringUser) : null;
    this.commentList = this.user.commentList;
    this.route.paramMap.subscribe(params => {
      this.recipeId = Number(params.get('id'));
    })

    this.commentSectionForm.patchValue({
      userId: this.user.id,
      recipeId: this.recipeId
    });
    console.log(this.user);
    this.getRecipeById();
    this.checkIfExistFavorite();
  }



  getRecipeById() {
    this.recipeService.findById(this.recipeId).subscribe(response => {
      this.recipe = response;
      console.log(this.recipe.imageUrl);
      // Tüm yorumlar için kullanıcı isimlerini tek seferde çek
      const userObservables = this.recipe.commentList.map(comment => 
        this.userService.findById(comment.userId)
      );
  
      forkJoin(userObservables).subscribe(users => {
        // Kullanıcı isimlerini commentList sırasıyla eşleştir
        this.usernameList = users.map(user => user.username);
      });
    });
  }
  popUp() {
    this.isOpen = !this.isOpen;
  }

  createComment() {
    this.commentService.createComment(this.commentSectionForm.value).subscribe(response => {
      this.getRecipeById();
      this.commentSectionForm.get('content')?.reset();
    })

  }
  canDeleteComment(userId: number): boolean {
    return this.user.id === userId;
  }
  deleteComment(id: number) {
    this.commentService.deleteComment(id).subscribe(response => {
      this.user.commentList = this.user.commentList.filter(x => x.id !== id);
      this.getRecipeById();
    })
  }

  findById(userId:number):User{
        this.userService.findById(userId).subscribe(response => {
            this.commentUser = response;
        })
        return this.commentUser;
  }

  toggleFavoriteButton(){
      const favorite:Favorite = {userId:this.user.id,recipeId:this.recipeId}
      this.favoriteService.toggleFavorite(favorite).subscribe(response => {
                this.checkIfExistFavorite();
                console.log(response);
      })
  }


  checkIfExistFavorite(){
      this.favoriteService.isExistsByUserIdAndRecipeId(this.user.id,this.recipeId).subscribe(response => {
            this.isFavorited = response;
      })
  }

}