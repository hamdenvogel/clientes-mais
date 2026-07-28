import { Component, Inject, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
import { Usuario } from './usuario';
import { AuthService } from '../auth.service';
import { Alert } from '../alert';
import { Constants } from '../shared/constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  username: string;
  password: string;
  listAlerts: Alert[] = [];
  timeOut = Constants.TIMEOUT;

  constructor(
    @Inject(DOCUMENT) private document: Document,
    private renderer: Renderer2,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.renderer.addClass(this.document.body, 'login-route');
    this.renderer.addClass(this.document.documentElement, 'login-route');
  }

  ngOnDestroy() {
    this.renderer.removeClass(this.document.body, 'login-route');
    this.renderer.removeClass(this.document.documentElement, 'login-route');
  }

  onSubmit() {
    this.listAlerts = [];

    if (!this.username || !this.password) {
      this.listAlerts.push({
        msg: 'Informe login e senha para continuar.',
        timeout: this.timeOut,
        type: 'warning'
      });
      return;
    }

    const usuario: Usuario = new Usuario();
    usuario.login = this.username;
    usuario.senha = this.password;

    this.authService.tentarLogar(usuario).subscribe(response => {
      const token = JSON.stringify(response);
      localStorage.setItem('token', token);
      localStorage.setItem('username', usuario.login);
      this.router.navigate(['/home']);
    }, errorResponse => {
      const backendMessage = errorResponse?.error?.errors?.[0];
      this.listAlerts.push({
        msg: backendMessage || 'Usuario e/ou senha incorreto(s)!',
        timeout: this.timeOut,
        type: 'danger'
      });
    });
  }

  preparaCadastrar(event: Event) {
    event.preventDefault();
    this.router.navigate(['/cadastro']);
  }
}
