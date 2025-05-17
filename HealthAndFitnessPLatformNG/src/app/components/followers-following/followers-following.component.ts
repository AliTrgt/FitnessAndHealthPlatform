import { Component, OnInit } from '@angular/core';
import { FollowService } from '../../service/follow/follow.service';
import { User } from '../../model/user';
import { UserService } from '../../service/user/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Follow } from '../../model/follow';
import { RouterLink } from '@angular/router';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-followers-following',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './followers-following.component.html',
  styleUrl: './followers-following.component.css'
})
export class FollowersFollowingComponent implements OnInit {
  activeTab: 'followers' | 'following' = 'followers';
  user!: User;
  followUsers: Follow[] = [];
  followingUsers: Follow[] = [];
  users: User[] = [];
  fusers: User[] = [];
  
  // Loading states
  isLoading = true;
  followersLoading = false;
  followingLoading = false;
  hasError = false;

  constructor(private followService: FollowService, private userService: UserService) {}
  
  ngOnInit() {
    this.isLoading = true;
    const stringToken = localStorage.getItem('currentUser');
    
    if (stringToken) {
      const parsedUser = JSON.parse(stringToken);
      
      this.userService.findById(parsedUser.id).subscribe({
        next: (userResponse) => {
          this.user = userResponse;
          this.followUsers = this.user.followers || [];
          this.followingUsers = this.user.following || [];
          
          // Fetch both followers and following data in parallel
          this.loadFollowersAndFollowing();
        },
        error: (error) => {
          console.error('Error loading user data:', error);
          this.isLoading = false;
          this.hasError = true;
        }
      });
    } else {
      this.isLoading = false;
    }
  }
  
  loadFollowersAndFollowing() {
    this.followersLoading = true;
    this.followingLoading = true;
    
    // Use forkJoin to load both followers and following users in parallel
    forkJoin({
      followers: this.getFollowerUsers().pipe(
        catchError(error => {
          console.error('Error loading followers:', error);
          this.hasError = true;
          return of([]);
        })
      ),
      following: this.getFollowingUsers().pipe(
        catchError(error => {
          console.error('Error loading following:', error);
          this.hasError = true;
          return of([]);
        })
      )
    }).subscribe({
      next: (result) => {
        this.users = result.followers;
        this.fusers = result.following;
        this.followersLoading = false;
        this.followingLoading = false;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading followers/following data:', error);
        this.followersLoading = false;
        this.followingLoading = false;
        this.isLoading = false;
        this.hasError = true;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
  
  getFollowerUsers(): Observable<User[]> {
    if (!this.followUsers.length) {
      return of([]);
    }
    
    // Map each follower to an Observable of the user details
    const userObservables = this.followUsers.map(follow => 
      this.userService.findById(follow.followingId)
    );
    
    // Combine all user observables into a single observable that emits an array
    return forkJoin(userObservables);
  }
  
  getFollowingUsers(): Observable<User[]> {
    if (!this.followingUsers.length) {
      return of([]);
    }
    
    // Map each following to an Observable of the user details
    const userObservables = this.followingUsers.map(follow => 
      this.userService.findById(follow.followerId)
    );
    
    // Combine all user observables into a single observable that emits an array
    return forkJoin(userObservables);
  }

  setTab(tab: 'followers' | 'following') {
    this.activeTab = tab;
  }
}