import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subreddit } from 'src/app/model/subreddit.model';
import { SubredditService } from '../subreddit.service';

@Component({
  selector: 'app-create-subreddit',
  templateUrl: './create-subreddit.component.html',
  styleUrls: ['./create-subreddit.component.css']
})
export class CreateSubredditComponent implements OnInit {

  createSubredditForm: FormGroup;
  subreddit: Subreddit;
  title = new FormControl('');
  description = new FormControl('');

  constructor(
    private router: Router, private subredditService: SubredditService
  ) {}

  ngOnInit(): void {
    
    this.createSubredditForm = new FormGroup({
      title: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required)
    });

    this.subreddit = {
      name: '',
      description: ''
    }
  }

  createSubreddit() {
    this.subreddit.name = this.createSubredditForm.get('title')?.value;
    this.subreddit.description = this.createSubredditForm.get('description')?.value;

    this.subredditService.createSubreddit(this.subreddit)
      .subscribe((data) => {
        this.router.navigateByUrl('/list-subreddits');
      }, error => {
        console.log('Error occurred while fetching the subreddits');
      });
  }

  discard() {
    this.router.navigateByUrl('/');
  }

}
