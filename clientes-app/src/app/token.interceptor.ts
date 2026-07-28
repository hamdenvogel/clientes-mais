import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor() {}

  private extractToken(tokenString: string | null): string | null {
    if (!tokenString) {
      return null;
    }

    try {
      const parsedToken = JSON.parse(tokenString);
      return parsedToken?.token || parsedToken?.accessToken || parsedToken?.jwt || null;
    } catch (error) {
      return tokenString;
    }
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    const jwt = this.extractToken(localStorage.getItem('token'));
    const url = request.url;
    const isApiRequest = url.startsWith(environment.apiURLBase) || url.startsWith('/api/');
    const isAuthRequest = url.includes('/api/auth/signin') || url.includes('/api/auth/signup');

    if (jwt && isApiRequest && !isAuthRequest) {
      request = request.clone({
        setHeaders: {
          Authorization: 'Bearer ' + jwt
        }
      });
    }

    return next.handle(request);
  }
}
