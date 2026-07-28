import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Alert } from '../alert';
import { Constants } from '../shared/constants';

@Component({
  selector: 'app-confirmar-cadastro',
  templateUrl: './confirmar-cadastro.component.html',
  styleUrls: ['./confirmar-cadastro.component.css']
})
export class ConfirmarCadastroComponent implements OnInit, OnDestroy {

  token = '';
  password = '';
  confirmPassword = '';
  email = '';
  tokenValido = false;
  loading = false;
  loadingReenvio = false;
  resendCooldownSeconds = 0;
  listAlerts: Alert[] = [];
  timeOut = Constants.TIMEOUT;

  passwordRules = {
    minLength: false,
    upper: false,
    lower: false,
    number: false,
    special: false,
    noWhitespace: false
  };

  private resendIntervalId: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    if (!this.token) {
      this.listAlerts.push({ msg: 'Token de confirmação não informado.', timeout: this.timeOut, type: 'danger' });
      return;
    }

    this.authService.validarTokenCadastro(this.token).subscribe(() => {
      this.tokenValido = true;
    }, errorResponse => {
      const errors = errorResponse?.error?.errors || ['Link inválido ou expirado.'];
      errors.forEach((erro) => this.listAlerts.push({ msg: this.normalizarMensagem(erro), timeout: this.timeOut, type: 'danger' }));
    });
  }

  ngOnDestroy(): void {
    if (this.resendIntervalId) {
      clearInterval(this.resendIntervalId);
    }
  }

  onPasswordChange() {
    const currentPassword = this.password || '';
    this.passwordRules.minLength = currentPassword.length >= 8;
    this.passwordRules.upper = /[A-Z]/.test(currentPassword);
    this.passwordRules.lower = /[a-z]/.test(currentPassword);
    this.passwordRules.number = /\d/.test(currentPassword);
    this.passwordRules.special = /[^A-Za-z0-9]/.test(currentPassword);
    this.passwordRules.noWhitespace = !/\s/.test(currentPassword);
  }

  senhaForte(): boolean {
    return Object.values(this.passwordRules).every((value) => value);
  }

  senhasConferem(): boolean {
    return !!this.password && this.password === this.confirmPassword;
  }

  concluirCadastro() {
    this.listAlerts = [];

    if (!this.tokenValido) {
      this.listAlerts.push({ msg: 'Link inválido ou expirado. Solicite um novo envio.', timeout: this.timeOut, type: 'danger' });
      return;
    }

    this.onPasswordChange();

    if (!this.senhaForte()) {
      this.listAlerts.push({ msg: 'A senha não atende aos requisitos de segurança.', timeout: this.timeOut, type: 'warning' });
      return;
    }

    if (!this.senhasConferem()) {
      this.listAlerts.push({ msg: 'As senhas informadas não conferem.', timeout: this.timeOut, type: 'warning' });
      return;
    }

    this.loading = true;
    this.authService.concluirCadastro({
      token: this.token,
      password: this.password,
      confirmPassword: this.confirmPassword
    }).subscribe(response => {
      this.loading = false;
      this.listAlerts.push({ msg: this.normalizarMensagem(response.message), timeout: this.timeOut, type: 'success' });
      setTimeout(() => this.router.navigate(['/login']), 1400);
    }, errorResponse => {
      this.loading = false;
      const errors = errorResponse?.error?.errors || ['Não foi possível concluir o cadastro.'];
      errors.forEach((erro) => this.listAlerts.push({ msg: this.normalizarMensagem(erro), timeout: this.timeOut, type: 'danger' }));
    });
  }

  reenviar() {
    if (this.resendCooldownSeconds > 0) {
      return;
    }

    if (!this.email || !this.emailValido(this.email)) {
      this.listAlerts.push({ msg: 'Informe um e-mail válido para reenviar o link.', timeout: this.timeOut, type: 'warning' });
      return;
    }

    this.loadingReenvio = true;
    this.authService.reenviarConfirmacao(this.email).subscribe(response => {
      this.loadingReenvio = false;
      this.listAlerts.push({ msg: this.normalizarMensagem(response.message), timeout: this.timeOut, type: 'info' });
      this.iniciarCooldownReenvio();
    }, errorResponse => {
      this.loadingReenvio = false;
      const errors = errorResponse?.error?.errors || ['Não foi possível reenviar o link de confirmação.'];
      errors.forEach((erro) => this.listAlerts.push({ msg: this.normalizarMensagem(erro), timeout: this.timeOut, type: 'danger' }));
    });
  }

  private iniciarCooldownReenvio() {
    if (this.resendIntervalId) {
      clearInterval(this.resendIntervalId);
    }

    this.resendCooldownSeconds = 60;
    this.resendIntervalId = setInterval(() => {
      this.resendCooldownSeconds -= 1;
      if (this.resendCooldownSeconds <= 0) {
        clearInterval(this.resendIntervalId);
      }
    }, 1000);
  }

  private emailValido(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
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
