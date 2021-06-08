import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { VotePayload } from './vote-button/vote.payload';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  SERVER_URL = environment.SERVER_URL;

  constructor(
    private http: HttpClient
  ) {}

  vote(votePayload: VotePayload): Observable<any> {
    return this.http
      .post(`${this.SERVER_URL}/api/votes`, votePayload);
  }

}
