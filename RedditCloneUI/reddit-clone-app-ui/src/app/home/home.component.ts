import { Component, OnInit } from '@angular/core';
import { Post } from '../model/post.model';
import { PostService } from '../shared/post.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  posts$: Array<Post> = [];

  constructor(
    private postService: PostService
  ) {
    this.postService.getAllPosts()
      .subscribe((data) => {
        this.posts$ = data;
      });
  }

  ngOnInit(): void {
  }

}
