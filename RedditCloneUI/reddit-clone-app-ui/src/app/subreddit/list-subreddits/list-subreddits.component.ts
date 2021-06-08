import { Component, OnInit } from '@angular/core';
import { Subreddit } from 'src/app/model/subreddit.model';
import { SubredditService } from '../subreddit.service';

@Component({
  selector: 'app-list-subreddits',
  templateUrl: './list-subreddits.component.html',
  styleUrls: ['./list-subreddits.component.css']
})
export class ListSubredditsComponent implements OnInit {

  subreddits: Array<Subreddit>;

  constructor(private subredditService: SubredditService) {}

  ngOnInit(): void {

    this.subredditService.getAllSubreddits()
      .subscribe(data => {
        this.subreddits = data;
      }, error => {
        console.log("Error while fetching all subreddits");
      });

  }

}
