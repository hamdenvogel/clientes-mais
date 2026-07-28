import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Alert } from '../alert';
import { Constants } from '../shared/constants';

@Component({
  selector: 'app-cadastro-solicitacao',
  templateUrl: './cadastro-solicitacao.component.html',
  styleUrls: ['./cadastro-solicitacao.component.css']
})
export class CadastroSolicitacaoComponent implements OnInit, OnDestroy {

  username = '';
  email = '';
  listAlerts: Alert[] = [];
  timeOut = Constants.TIMEOUT;
  loading = false;

  constructor(
    @Inject(DOCUMENT) private document: Document,
    private renderer: Renderer2,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.renderer.addClass(this.document.body, 'login-route');
    this.renderer.addClass(this.document.documentElement, 'login-route');
  }

  ngOnDestroy() {
    this.renderer.removeClass(this.document.body, 'login-route');
    this.renderer.removeClass(this.document.documentElement, 'login-route');
  }

  solicitarCadastro() {
    this.listAlerts = [];
    this.loading = true;

    this.authService.iniciarCadastro({ username: this.username, email: this.email })
      .subscribe(response => {
        this.loading = false;
        this.listAlerts.push({
          msg: this.normalizarMensagem(response.message) || 'Solicitação recebida. Enviamos um e-mail para confirmação.',
          timeout: this.timeOut,
          type: 'success'
        });
        this.username = '';
        this.email = '';
      }, errorResponse => {
        this.loading = false;
        const errors = errorResponse?.error?.errors || ['Não foi possível iniciar o cadastro.'];
        errors.forEach((erro) => {
          this.listAlerts.push({ msg: this.normalizarMensagem(erro), timeout: this.timeOut, type: 'danger' });
        });
      });
  }

  voltarLogin() {
    this.router.navigate(['/login']);
  }

  private normalizarMensagem(msg: string): string {
    if (!msg) {
      return msg;
    }

    return msg
      .replace(/\bnao\b/gi, 'não')
      .replace(/\bpossivel\b/gi, 'possível')
      .replace(/\bconfirmacao\b/gi, 'confirmação')
      .replace(/\binvalido\b/gi, 'inválido')
      .replace(/\bobrigatorio\b/gi, 'obrigatório')
      .replace(/\bseguranca\b/gi, 'segurança')
      .replace(/\busuario\b/gi, 'usuário')
      .replace(/\bvoce\b/gi, 'você')
      .replace(/\be-mail\s+e\s+obrigatorio\b/gi, 'e-mail é obrigatório');
  }
}

