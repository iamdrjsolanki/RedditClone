import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CommentPayload } from './comment.payload';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  SERVER_URL = environment.SERVER_URL;

  constructor(
    private httpClient: HttpClient
  ) {}

  getAllCommentsForPost(postId: number): Observable<CommentPayload[]> {
    console.log(`${this.SERVER_URL}/api/comments/by-postId/` + postId);
    return this.httpClient
      .get<CommentPayload[]>(`${this.SERVER_URL}/api/comments/by-postId/` + postId);
  }

  postComment(commentPayload: CommentPayload): Observable<any> {
    return this.httpClient
      .post<any>(`${this.SERVER_URL}/api/comments`, commentPayload);
  }

  getAllCommentsByUser(name: string) {
    return this.httpClient
      .get<CommentPayload[]>(`${this.SERVER_URL}/api/comments/by-user/` + name);
  }

}
