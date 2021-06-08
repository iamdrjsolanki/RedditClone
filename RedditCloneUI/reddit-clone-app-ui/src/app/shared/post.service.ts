import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Post } from '../model/post.model';
import { CreatePostPayload } from '../post/create-post/create-post.payload';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  SERVER_URL = environment.SERVER_URL;

  constructor(
    private http: HttpClient
  ) {}

  getAllPosts(): Observable<Array<Post>> {
    return this.http
      .get<Array<Post>>(`${this.SERVER_URL}/api/post`)
  }

  createPost(postPayload: CreatePostPayload): Observable<any> {
    console.log(postPayload);
    return this.http
      .post(`${this.SERVER_URL}/api/post`, postPayload);
  }

  getPost(postId: number): Observable<Post> {
    return this.http
      .get<Post>(`${this.SERVER_URL}/api/post/`+postId);
  }

  getAllPostsByUser(name: string): Observable<Post[]> {
    return this.http
      .get<Post[]>(`${this.SERVER_URL}/api/post/by-user/` + name);
  }

}
