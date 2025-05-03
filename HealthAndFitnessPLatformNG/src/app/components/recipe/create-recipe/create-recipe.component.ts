

import { ChangeDetectorRef, Component, OnInit, resolveForwardRef } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RecipeService } from '../../../service/recipe/recipe.service';
import { response } from 'express';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { User } from '../../../model/user';
import { IngredientService } from '../../../service/ingredient/ingredient.service';
import { Ingredient } from '../../../model/ingredient';
import { Recipe } from '../../../model/recipe';
import { ImageUploadService } from '../../../service/imageUpload/image-upload.service';

@Component({
  selector: 'app-create-recipe',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule,RouterLink],
  templateUrl: './create-recipe.component.html',
  styleUrl: './create-recipe.component.css'
})
export class CreateRecipeComponent implements OnInit {
  photoPreview: string | ArrayBuffer | null = null;
  createForm!: FormGroup;
  user!: User;
  recipe!: Recipe;
  recipeId!: number;
  isEditMode: boolean = false;
  selectedPhoto!: File;
  constructor(private recipeService: RecipeService, private fb: FormBuilder, private route: Router, private imageUploadService:ImageUploadService, private routE: ActivatedRoute,private ingredientService:IngredientService) { }
  ngOnInit(): void {
    const userToken = localStorage.getItem('currentUser');
    userToken ? this.user = JSON.parse(userToken) : null

    this.createForm = this.fb.group({
      id: [''],
      title: ['', Validators.required],
      prepTime: ['', Validators.required],
      calories: ['', Validators.required],
      description: [''],
      userId: [''],
      imageUrl:[''],
      ingredients: this.fb.array([]),
      instructions: this.fb.array([])
    })

    this.routE.paramMap.subscribe(params => {
      this.recipeId = Number(params.get('id'));
      if (this.recipeId) {
        this.isEditMode = true;
        this.getRecipeById();
      }
      else {
        this.addInstruction();
        this.addIngredient();
      }
    })

  }

  getRecipeById() {
    this.recipeService.findById(this.recipeId).subscribe(response => {
      this.recipe = response;
      this.fillFormWithRecipe(this.recipe);
    })
  }

  createRecipe() {
    const linkedInstruction = this.instructions.value.join('\n ');
    const formData = {
      ...this.createForm.value,
      userId: this.user.id,
      instructions: linkedInstruction,
      imageUrl: this.isEditMode && this.recipe ? this.recipe.imageUrl : '',
      ingredientList: this.ingredients.value.map((ing: any) => ({
        id : ing.id,
        name: ing.name,
        quantity: ing.quantity
      }))
    }
    if (this.isEditMode) {
      this.recipeService.updateRecipe(this.recipeId, formData).subscribe((response) => {
        if(this.selectedPhoto){
            this.imageUploadService.uploadRecipeImagePhoto(response.id,this.selectedPhoto).subscribe(() => {
                console.log("Foto Güncellendi");
                this.route.navigate(["/myrecipes"]);
            })
        }
        this.route.navigate(["/myrecipes"]);
      })
    }
    if(!this.isEditMode) {
      this.recipeService.createRecipe(formData).subscribe(response => {
        if(this.selectedPhoto){
            this.imageUploadService.uploadRecipeImagePhoto(response.id,this.selectedPhoto).subscribe(() => {
                console.log("Foto Eklendi");
                this.route.navigate(["/myrecipes"]);
            })
        }
      })

    }

  }

  onPhotoSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedPhoto = file; // ⬅️ Fotoğrafı sakla

      const reader = new FileReader();
      reader.onload = () => this.photoPreview = reader.result;
      reader.readAsDataURL(file);
    }
  }

  fillFormWithRecipe(recipe: Recipe) {
    this.createForm.patchValue({
      title: recipe.title,
      prepTime: recipe.prepTime,
      calories: recipe.calories,
      description: recipe.description
    })
    this.ingredients.clear();
    this.instructions.clear();

    recipe.ingredientList.forEach((ingredient: Ingredient) => {
      this.ingredients.push(this.fb.group({
        id : [ingredient.id],
        name: [ingredient.name, Validators.required],
        quantity: [ingredient.quantity, Validators.required]
      }));
    });

    if (recipe.instructions) {
      const instructionLines = recipe.instructions.split('\n');
      instructionLines.forEach((step: string) => {
        this.instructions.push(this.fb.control(step.trim(), Validators.required));
      });
    }
  }

  get ingredients(): FormArray {
    return this.createForm.get('ingredients') as FormArray;
  }

  get instructions(): FormArray {
    return this.createForm.get('instructions') as FormArray;
  }

  addIngredient() {
    this.ingredients.push(this.fb.group({
      id: [null],
      name: [''],
      quantity: ['']
    }));
  }

  addInstruction() {
    this.instructions.push(this.createStep());
  }

  removeIngredient(index: number) {
    this.ingredients.removeAt(index);
  }

  removeInstruction(index: number) {
    this.instructions.removeAt(index);
  }

  createStep(): FormControl {
    return this.fb.control('', Validators.required);
  }

  get instructionControls(): FormControl[] {
    return this.instructions.controls as FormControl[];
  }


 updateIngredient(ingredientId:number,ingredient:Ingredient){
    return this.ingredientService.updateIngredient(ingredientId,ingredient).subscribe(response => {
          console.log(response);
    })
 }

}



