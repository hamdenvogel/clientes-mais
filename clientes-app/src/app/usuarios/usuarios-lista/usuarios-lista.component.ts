import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Alert } from 'src/app/alert';
import { Constants } from 'src/app/shared/constants';
import { MaskUtil } from 'src/app/shared/utils/mask.util';
import { Usuario } from '../usuario';
import { UsuariosService } from '../../usuarios.service';

@Component({
  selector: 'app-usuarios-lista',
  templateUrl: './usuarios-lista.component.html',
  styleUrls: ['./usuarios-lista.component.css']
})
export class UsuariosListaComponent implements OnInit {
  usuarios: Usuario[] = [];
  usuariosFiltrados: Usuario[] = [];
  config = {
    itemsPerPage: 6,
    currentPage: 1
  };
  campoPesquisa = '';
  loading = false;
  listAlerts: Alert[] = [];
  timeout = Constants.TIMEOUT;

  constructor(
    private usuariosService: UsuariosService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const { mensagem } = window.history.state;
    if (mensagem) {
      this.listAlerts.push({
        msg: mensagem,
        timeout: this.timeout,
        type: 'success'
      });
    }

    this.consultar();
  }

  consultar(): void {
    this.loading = true;
    this.usuariosService.obterTodos().subscribe({
      next: resposta => {
        this.usuarios = resposta;
        this.usuariosFiltrados = [...resposta];
        this.loading = false;
      },
      error: errorResponse => {
        const mensagens = errorResponse?.error?.errors || ['Erro ao buscar usuarios.'];
        mensagens.forEach(msg => this.adicionarAlerta(msg, 'danger'));
        this.loading = false;
      }
    });
  }

  pesquisar(): void {
    const termo = this.campoPesquisa.trim().toLowerCase();
    if (!termo) {
      this.usuariosFiltrados = [...this.usuarios];
      return;
    }

    this.usuariosFiltrados = this.usuarios.filter(usuario =>
      (usuario.username || '').toLowerCase().includes(termo)
      || (usuario.email || '').toLowerCase().includes(termo)
      || (usuario.cpf || '').toLowerCase().includes(termo)
      || (usuario.telefone || '').toLowerCase().includes(termo)
      || (usuario.cidade || '').toLowerCase().includes(termo)
      || (usuario.roles || []).join(' ').toLowerCase().includes(termo));
  }

  onKeyUp(): void {
    this.pesquisar();
  }

  novoCadastro(): void {
    this.router.navigate(['/usuarios/form']);
  }

  editar(usuario: Usuario): void {
    this.router.navigate(['/usuarios/form', usuario.id]);
  }

  deletar(usuario: Usuario): void {
    if (!confirm(`Confirma a exclusao do usuario ${usuario.username}?`)) {
      return;
    }

    this.usuariosService.deletar(usuario.id).subscribe({
      next: response => {
        this.adicionarAlerta(response.mensagem || 'Usuario deletado com sucesso.', 'success');
        this.consultar();
      },
      error: errorResponse => {
        const mensagens = errorResponse?.error?.errors || ['Erro ao deletar usuario.'];
        mensagens.forEach(msg => this.adicionarAlerta(msg, 'danger'));
      }
    });
  }

  private adicionarAlerta(msg: string, type: string): void {
    this.listAlerts.push({ msg, timeout: this.timeout, type });
  }

  onPageChange(event: number): void {
    this.config.currentPage = event;
  }

  formatCpf(value: string): string {
    return MaskUtil.applyCpf(value);
  }

  formatTelefone(value: string): string {
    return MaskUtil.applyPhone(value);
  }
}
