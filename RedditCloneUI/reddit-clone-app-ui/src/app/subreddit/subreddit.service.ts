import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Subreddit } from '../model/subreddit.model';

@Injectable({
  providedIn: 'root'
})
export class SubredditService {

  SERVER_URL = environment.SERVER_URL;

  constructor(
    private http: HttpClient
  ) {}

  getAllSubreddits(): Observable<Array<Subreddit>> {
    return this.http
      .get<Array<Subreddit>>(`${this.SERVER_URL}/api/subreddit`);
  }

  createSubreddit(subreddit: Subreddit): Observable<any> {
    return this.http
      .post(`${this.SERVER_URL}/api/subreddit`, subreddit);
  }

}
