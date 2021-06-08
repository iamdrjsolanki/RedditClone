import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Post } from 'src/app/model/post.model';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PostTileComponent implements OnInit {

  @Input() posts: Array<Post>;
  faComments = faComments;

  constructor(
    private router: Router
  ) {}

  ngOnInit(): void {
  }

  goToPost(postId: number) {
    this.router.navigateByUrl("/view-post/" + postId);
  }

}
