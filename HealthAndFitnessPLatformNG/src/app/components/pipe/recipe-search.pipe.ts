import { Pipe, PipeTransform } from '@angular/core';
import { Recipe } from '../../model/recipe';

@Pipe({
  name: 'recipeSearch',
  standalone: true
})
export class RecipeSearchPipe implements PipeTransform {

  transform(recipes: Recipe[] | null, searchText: string): Recipe[] {
    if (!recipes || !searchText) {
      return recipes ?? [];
    }

    searchText = searchText.toLowerCase();

    return recipes.filter(recipe =>
      recipe.title.toLowerCase().includes(searchText)
    );
  }

}
