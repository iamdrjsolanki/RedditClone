import { Component, Input, OnInit } from '@angular/core';
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { Post } from 'src/app/model/post.model';
import { PostService } from '../post.service';
import { VoteService } from '../vote.service';
import { VoteType } from './vote-type';
import { VotePayload } from './vote.payload';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.css']
})
export class VoteButtonComponent implements OnInit {

  @Input() post: Post;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  votePayload: VotePayload;
  upvoteColor: string;
  downvoteColor: string;
  isLoggedIn: boolean;

  constructor(
    private voteService: VoteService, private authService: AuthService,
    private postService: PostService, private toastrService: ToastrService
  ) {
    this.votePayload = {
      voteType: undefined!,
      postId: undefined!
    }

    this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
  }

  ngOnInit(): void {
    this.updateVoteDetails();
  }

  upvotePost() {
    this.votePayload.voteType = VoteType.UPVOTE;
    this.vote();
    this.downvoteColor = '';
  }

  downvotePost() {
    this.votePayload.voteType = VoteType.DOWNVOTE;
    this.vote();
    this.upvoteColor = '';
  }

  private vote() {
    this.votePayload.postId = this.post.id;
    this.voteService.vote(this.votePayload)
      .subscribe(() => {
        this.updateVoteDetails();
      }, error => {
        this.toastrService.error(error.error.message);
        throwError(error);
      });
  }

  private updateVoteDetails() {
    this.postService.getPost(this.post.id)
      .subscribe(post => {
        this.post = post;
      });
  }

}
