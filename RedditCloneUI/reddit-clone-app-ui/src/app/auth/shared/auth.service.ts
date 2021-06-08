import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable, Output } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginRequestPayload } from '../login/login-request.payload';
import { LoginResponsePayload } from '../login/login-response.payload';
import { SignupRequestPayload } from '../signup/signup-request.payload';
import { map, tap } from 'rxjs/operators'
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  @Output() username: EventEmitter<string> = new EventEmitter();
  private SERVER_URL = environment.SERVER_URL;
  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    username: this.getUsername()
  }

  constructor(
    private http: HttpClient,
    private localWebStorage: LocalStorageService
  ) {}

  signup(signupReqPayload: SignupRequestPayload) : Observable<any> {
    return this.http.post(`${this.SERVER_URL}/api/auth/signup`, signupReqPayload, { responseType: 'text' });
  }

  login(loginReqPayload: LoginRequestPayload) : Observable<boolean> {
    return this.http
      .post<LoginResponsePayload>(`${this.SERVER_URL}/api/auth/login`, loginReqPayload)
      .pipe(
        map(data => {
          this.localWebStorage.store('authenticationToken', data.authenticationToken);
          this.localWebStorage.store('refreshToken', data.refreshToken);
          this.localWebStorage.store('expiresAt', data.expiresAt);
          this.localWebStorage.store('username', data.username);

          this.loggedIn.emit(true);
          this.username.emit(data.username);
          return true;
        })
      );
  }

  isLoggedIn(): boolean {
    return this.getJwtToken() != null;
  }

  getJwtToken() {
    return this.localWebStorage.retrieve('authenticationToken');
  }

  getRefreshToken() {
    return this.localWebStorage.retrieve('refreshToken');
  }

  getUsername() {
    return this.localWebStorage.retrieve('username');
  }

  getExpiresAt() {
    return this.localWebStorage.retrieve('expiresAt');
  }

  refreshToken() {
    return this.http
        .post<LoginResponsePayload>(`${this.SERVER_URL}/api/auth/refresh/token`, this.refreshTokenPayload)
        .pipe(
          tap(response => {
            this.localWebStorage.clear('authenticationToken');
            this.localWebStorage.clear('expiresAt');

            this.localWebStorage.store('authenticationToken', response.authenticationToken);
            this.localWebStorage.store('expiresAt', response.expiresAt);
          })
        );
  }

  logout() {
    this.http
      .post(`${this.SERVER_URL}/api/auth/logout`, this.refreshTokenPayload, { responseType: 'text' })
      .subscribe(data => {
        console.log(data);
      }, error => {
        throwError(error);
      })
    this.localWebStorage.clear('authenticationToken');
    this.localWebStorage.clear('username');
    this.localWebStorage.clear('refreshToken');
    this.localWebStorage.clear('expiresAt');
  }

}
