import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Usuario } from './login/usuario';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient
  ) { }

  private getAuthData(): any {
    const tokenString = localStorage.getItem('token');
    if (!tokenString) {
      return null;
    }

    try {
      return JSON.parse(tokenString);
    } catch (error) {
      if (typeof tokenString === 'string' && tokenString.trim().length > 0) {
        return { token: tokenString };
      }
      this.encerrarSessao();
      return null;
    }
  }

  obterToken() {
    const authData = this.getAuthData();
    if (authData) {
      return authData.token || authData.accessToken || authData.jwt || null;
    }
    return null;
  }

  encerrarSessao() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
  }

  getUsuarioAutenticado() {
    const token = this.obterToken();
    if (token) {
      const usuarioStorage = localStorage.getItem('username');
      if (usuarioStorage) {
        return usuarioStorage;
      }

      try {
		const payload = this.decodeToken(token);
		return payload?.sub || payload?.username || null;
      } catch (error) {
        this.encerrarSessao();
        return null;
      }
    }
    return null;
  }

  isAuthenticated(): boolean {
    const token = this.obterToken();
    if (token) {
      try {
        const expired = this.isTokenExpired(token);
        if (expired) {
          this.encerrarSessao();
        }
        return !expired;
      } catch (error) {
        this.encerrarSessao();
        return false;
      }
    }
    return false;
  }


  iniciarCadastro(payload: { username: string; email: string }): Observable<any> {
    const signupInitURL = environment.apiURLBase + '/api/auth/registration/signup-init';
    return this.http.post<any>(signupInitURL, payload);
  }

  validarTokenCadastro(token: string): Observable<any> {
    const validateTokenUrl = environment.apiURLBase + '/api/auth/registration/validate-token';
    return this.http.get<any>(validateTokenUrl, { params: { token } });
  }

  concluirCadastro(payload: { token: string; password: string; confirmPassword: string }): Observable<any> {
    const completeUrl = environment.apiURLBase + '/api/auth/registration/complete';
    return this.http.post<any>(completeUrl, payload);
  }

  reenviarConfirmacao(email: string): Observable<any> {
    const resendUrl = environment.apiURLBase + '/api/auth/registration/resend-confirmation';
    return this.http.post<any>(resendUrl, { email });
  }

  tentarLogar(usuario: Usuario): Observable<any> {
    const signinURL = environment.apiURLBase + '/api/auth/signin';
    return this.http.post<any>(signinURL, usuario);
  }

  private decodeToken(token: string): any {
    const parts = token.split('.');
    if (parts.length < 2) {
      throw new Error('Token JWT invalido.');
    }

    const payload = parts[1]
      .replace(/-/g, '+')
      .replace(/_/g, '/');
    const padding = payload.length % 4;
    const normalizedPayload = padding === 0 ? payload : payload + '='.repeat(4 - padding);

    return JSON.parse(atob(normalizedPayload));
  }

  private isTokenExpired(token: string): boolean {
    const payload = this.decodeToken(token);
    if (!payload?.exp) {
      return false;
    }

    const expiresAt = Number(payload.exp) * 1000;
    return Number.isFinite(expiresAt) ? expiresAt <= Date.now() : false;
  }

}
