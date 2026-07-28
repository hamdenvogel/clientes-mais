import { FormsModule } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { CadastroSolicitacaoComponent } from './cadastro-solicitacao.component';
import { AuthService } from '../auth.service';

describe('CadastroSolicitacaoComponent', () => {
  let component: CadastroSolicitacaoComponent;
  let fixture: ComponentFixture<CadastroSolicitacaoComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async(() => {
    authService = jasmine.createSpyObj('AuthService', ['iniciarCadastro']);
    router = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [CadastroSolicitacaoComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastroSolicitacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve iniciar cadastro e limpar formulario em caso de sucesso', () => {
    authService.iniciarCadastro.and.returnValue(of({ message: 'Email enviado.' }));
    component.username = 'novo';
    component.email = 'novo@test.com';

    component.solicitarCadastro();

    expect(authService.iniciarCadastro).toHaveBeenCalledWith({ username: 'novo', email: 'novo@test.com' });
    expect(component.username).toBe('');
    expect(component.email).toBe('');
    expect(component.listAlerts[0].type).toBe('success');
  });

  it('deve exibir erros do backend em caso de falha', () => {
    authService.iniciarCadastro.and.returnValue(throwError({ error: { errors: ['E-mail ja utilizado.'] } }));
    component.username = 'novo';
    component.email = 'novo@test.com';

    component.solicitarCadastro();

    expect(component.listAlerts[0].msg).toBe('E-mail ja utilizado.');
    expect(component.loading).toBeFalse();
  });

  it('deve voltar para a tela de login', () => {
    component.voltarLogin();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});

