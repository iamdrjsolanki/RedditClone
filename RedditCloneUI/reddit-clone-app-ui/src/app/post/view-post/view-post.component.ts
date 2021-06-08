import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { throwError } from 'rxjs';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/shared/post.service';
import { CommentPayload } from '../../comment/comment.payload';
import { CommentService } from '../../comment/comment.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent implements OnInit {

  postId: number;
  post: Post;
  commentForm: FormGroup;
  comments: Array<CommentPayload>;
  commentPayload: CommentPayload;

  constructor(
    private activatedRoute: ActivatedRoute, private postService: PostService,
    private commentService: CommentService
  ) {
    this.postId = this.activatedRoute.snapshot.params.id;
    
    this.postService.getPost(this.postId)
      .subscribe(data => {
        console.log(data);
        this.post = data;
      }, error => {
        throwError(error);
      });

    this.commentForm = new FormGroup({
      text: new FormControl('', Validators.required)
    });
    
    this.commentPayload = {
      text: '',
      postId: this.postId
    };
  }

  ngOnInit(): void {
    this.getPostById();
    this.getCommentsForPost();
  }

  postComment() {
    this.commentPayload.text = this.commentForm.get('text')?.value;
    this.commentService.postComment(this.commentPayload)
      .subscribe(data => {
        this.commentForm.get('text')?.setValue('');
        this.getCommentsForPost();
      }, error => {
        throwError(error);
      });
  }

  private getPostById() {
    this.postService.getPost(this.postId)
      .subscribe(data => {
        this.post = data;
      }, error => {
        throwError(error);
      });
  }

  private getCommentsForPost() {
    console.log(this.postId);
    this.commentService.getAllCommentsForPost(this.postId)
      .subscribe(data => {
        console.log(data);
        this.comments = data;
      }, error => {
        throwError(error);
      });
  }

}
