import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { throwError } from 'rxjs';
import { CommentPayload } from 'src/app/comment/comment.payload';
import { CommentService } from 'src/app/comment/comment.service';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  name: string;
  posts: Post[];
  comments: CommentPayload[];
  postLength: number;
  commentLength: number;

  constructor(
    public activatedRoute: ActivatedRoute, private postService: PostService, 
    public commentService: CommentService
  ) {
    this.name = this.activatedRoute.snapshot.params.name;

    this.postService.getAllPostsByUser(this.name)
      .subscribe(data => {
        this.posts = data;
        this.postLength = data.length;
      }, error => {
        throwError(error);
      });

    this.commentService.getAllCommentsByUser(this.name)
      .subscribe(data => {
        this.comments = data;
        this.commentLength = data.length;
      }, error => {
        throwError(error);
      });
  }

  ngOnInit(): void {
  }

}
