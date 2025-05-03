import { Component, OnInit } from '@angular/core';
import { FollowService } from '../../service/follow/follow.service';
import { User } from '../../model/user';
import { UserService } from '../../service/user/user.service';
import { response } from 'express';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Follow } from '../../model/follow';
import { CommentService } from '../../service/comment/comment.service';

@Component({
  selector: 'app-followers-following',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './followers-following.component.html',
  styleUrl: './followers-following.component.css'
})
export class FollowersFollowingComponent implements OnInit {
  activeTab: 'followers' | 'following' = 'followers';
  user!:User;
  followUsers:Follow[] = [];
  followingUsers:Follow[] = [];
  users:User[] = [];
  fusers:User[] = [];
  constructor(private followService:FollowService,private userService:UserService){
  }
  
  
  ngOnInit() {
      const stringToken = localStorage.getItem('currentUser');
      if (stringToken) {
        const parsedUser = JSON.parse(stringToken);
        this.userService.findById(parsedUser.id).subscribe(userResponse => {
          this.user = userResponse;
          this.followUsers = this.user.followers;
          this.followingUsers = this.user.following;
    
          this.getFollowerObject();
          this.getFollowingObject();
        });
      }
    }
    

  getFollowerObject(){
        this.followUsers.map(follow => {
              this.userService.findById(follow.followingId).subscribe(response => {
                    this.users.push(response);
                    console.log(this.users);
              })
        })
  }

  getFollowingObject(){
      this.followingUsers.map(follow => {
            this.userService.findById(follow.followerId).subscribe(response => {
                  this.fusers.push(response);
            })
      })
  }

  setTab(tab: 'followers' | 'following'){
      this.activeTab = tab;
    console.log(tab);
  }


}