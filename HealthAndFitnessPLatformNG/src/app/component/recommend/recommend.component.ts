import { Component } from '@angular/core';

@Component({
  selector: 'app-recommend',
  standalone: true,
  imports: [],
  templateUrl: './recommend.component.html',
  styleUrl: './recommend.component.css'
})
export class RecommendComponent {
  showRecommendations = false;
  isLoading = false;

  getRecommendations() {
    this.isLoading = true;
    setTimeout(() => {
      this.isLoading = false;
      this.showRecommendations = true;
    }, 1500);
  }
}
