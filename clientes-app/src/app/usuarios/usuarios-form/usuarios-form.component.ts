import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Alert } from 'src/app/alert';
import { Constants } from 'src/app/shared/constants';
import { MaskUtil } from 'src/app/shared/utils/mask.util';
import { Usuario } from '../usuario';
import { UsuariosService } from '../../usuarios.service';

@Component({
  selector: 'app-usuarios-form',
  templateUrl: './usuarios-form.component.html',
  styleUrls: ['./usuarios-form.component.css']
})
export class UsuariosFormComponent implements OnInit, OnDestroy {
  usuario: Usuario = new Usuario();
  id: number;
  success = false;
  errors: string[];
  perfisDisponiveis: string[] = [];
  listAlerts: Alert[] = [];
  timeout = Constants.TIMEOUT;

  constructor(
    private usuariosService: UsuariosService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    @Inject(DOCUMENT) private document: Document
  ) { }

  ngOnInit(): void {
    this.document.body.classList.add('usuarios-form-no-vscroll');
    this.usuario.ativo = true;
    this.usuario.roles = ['ROLE_USER'];
    this.carregarPerfis();

    const params: Observable<Params> = this.activatedRoute.params;
    params.subscribe(urlParams => {
      this.id = +urlParams['id'];
      if (this.id) {
        this.usuariosService.obterPorId(this.id).subscribe({
          next: response => {
            this.usuario = response;
            this.usuario.password = '';
            this.usuario.ativo = this.usuario.ativo !== false;
            this.aplicarMascarasUsuario();
          },
          error: () => {
            this.usuario = new Usuario();
            this.usuario.ativo = true;
            this.usuario.roles = ['ROLE_USER'];
          }
        });
      }
    });
  }

  carregarPerfis(): void {
    this.usuariosService.listarPerfis().subscribe({
      next: response => this.perfisDisponiveis = response,
      error: () => this.perfisDisponiveis = ['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN']
    });
  }

  onRoleToggle(role: string, marcado: boolean): void {
    const atuais = this.usuario.roles || [];
    if (marcado) {
      if (!atuais.includes(role)) {
        atuais.push(role);
      }
    } else {
      this.usuario.roles = atuais.filter(item => item !== role);
    }

    if (!this.usuario.roles || this.usuario.roles.length === 0) {
      this.usuario.roles = ['ROLE_USER'];
    }
  }

  isRoleSelected(role: string): boolean {
    return (this.usuario.roles || []).includes(role);
  }

  onSubmit(): void {
    if (!this.usuario.cpf || !this.usuario.cpf.trim()) {
      this.adicionarAlerta('CPF e obrigatorio.', 'danger');
      return;
    }

    if (!this.id && (!this.usuario.password || this.usuario.password.trim().length < 4)) {
      this.adicionarAlerta('A senha e obrigatoria e deve ter ao menos 4 caracteres.', 'danger');
      return;
    }

    const payload = this.buildUsuarioPayload();

    if (this.id) {
      this.usuariosService.atualizar(payload).subscribe({
        next: response => {
          this.success = true;
          this.router.navigate(['/usuarios/lista'], { state: { mensagem: response.mensagem } });
        },
        error: errorResponse => this.mostrarErros(errorResponse)
      });
      return;
    }

    this.usuariosService.salvar(payload).subscribe({
      next: response => {
        this.success = true;
        const mensagem = response.infoResponseDTO?.mensagem || 'Usuario criado com sucesso.';
        this.router.navigate(['/usuarios/lista'], { state: { mensagem } });
      },
      error: errorResponse => this.mostrarErros(errorResponse)
    });
  }

  limpar(): void {
    this.usuario = new Usuario();
    this.usuario.ativo = true;
    this.usuario.roles = ['ROLE_USER'];
  }

  onCpfChange(value: string): void {
    this.usuario.cpf = MaskUtil.applyCpf(value);
  }

  onTelefoneChange(value: string): void {
    this.usuario.telefone = MaskUtil.applyPhone(value);
  }

  onCepChange(value: string): void {
    this.usuario.cep = MaskUtil.applyCep(value);
  }

  voltarParaListagem(): void {
    this.router.navigate(['/usuarios/lista']);
  }

  private mostrarErros(errorResponse: any): void {
    this.errors = errorResponse?.error?.errors || ['Erro ao salvar usuario.'];
    this.errors.forEach(erro => this.adicionarAlerta(erro, 'danger'));
  }

  private adicionarAlerta(msg: string, type: string): void {
    this.listAlerts.push({ msg, timeout: this.timeout, type });
  }

  private aplicarMascarasUsuario(): void {
    this.usuario.cpf = MaskUtil.applyCpf(this.usuario.cpf);
    this.usuario.telefone = MaskUtil.applyPhone(this.usuario.telefone);
    this.usuario.cep = MaskUtil.applyCep(this.usuario.cep);
  }

  private buildUsuarioPayload(): Usuario {
    return {
      ...this.usuario,
      cpf: MaskUtil.digitsOnly(this.usuario.cpf),
      telefone: MaskUtil.digitsOnly(this.usuario.telefone),
      cep: MaskUtil.digitsOnly(this.usuario.cep)
    };
  }

  ngOnDestroy(): void {
    this.document.body.classList.remove('usuarios-form-no-vscroll');
  }
}
