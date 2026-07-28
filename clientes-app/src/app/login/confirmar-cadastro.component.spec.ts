import { FormsModule } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed, async, discardPeriodicTasks, fakeAsync, tick } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { ConfirmarCadastroComponent } from './confirmar-cadastro.component';
import { AuthService } from '../auth.service';

describe('ConfirmarCadastroComponent', () => {
  let component: ConfirmarCadastroComponent;
  let fixture: ComponentFixture<ConfirmarCadastroComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async(() => {
    authService = jasmine.createSpyObj('AuthService', [
      'validarTokenCadastro',
      'concluirCadastro',
      'reenviarConfirmacao'
    ]);
    router = jasmine.createSpyObj('Router', ['navigate']);
    authService.validarTokenCadastro.and.returnValue(of({ valid: true }));

    TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [ConfirmarCadastroComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: convertToParamMap({ token: 'abc-token' })
            }
          }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmarCadastroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve validar o token na inicializacao', () => {
    expect(authService.validarTokenCadastro).toHaveBeenCalledWith('abc-token');
    expect(component.tokenValido).toBeTrue();
  });

  it('deve impedir conclusao com senha fraca', () => {
    component.password = '123';
    component.confirmPassword = '123';

    component.concluirCadastro();

    expect(authService.concluirCadastro).not.toHaveBeenCalled();
    expect(component.listAlerts[0].msg).toContain('não atende');
  });

  it('deve impedir conclusao quando as senhas forem diferentes', () => {
    component.password = 'Senha@123';
    component.confirmPassword = 'Senha@124';

    component.concluirCadastro();

    expect(authService.concluirCadastro).not.toHaveBeenCalled();
    expect(component.listAlerts[0].msg).toContain('não conferem');
  });

  it('deve concluir cadastro com senha forte', fakeAsync(() => {
    authService.concluirCadastro.and.returnValue(of({ message: 'Cadastro concluido.' }));
    component.password = 'Senha@123';
    component.confirmPassword = 'Senha@123';

    component.concluirCadastro();
    tick(1400);

    expect(authService.concluirCadastro).toHaveBeenCalledWith({
      token: 'abc-token',
      password: 'Senha@123',
      confirmPassword: 'Senha@123'
    });
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));

  it('deve validar e-mail antes de reenviar', () => {
    component.email = 'email-invalido';

    component.reenviar();

    expect(authService.reenviarConfirmacao).not.toHaveBeenCalled();
    expect(component.listAlerts[0].msg).toContain('e-mail válido');
  });

  it('deve iniciar cooldown ao reenviar com sucesso', fakeAsync(() => {
    authService.reenviarConfirmacao.and.returnValue(of({ message: 'Link reenviado.' }));
    component.email = 'user@test.com';

    component.reenviar();
    tick(1000);

    expect(authService.reenviarConfirmacao).toHaveBeenCalledWith('user@test.com');
    expect(component.resendCooldownSeconds).toBe(59);
    component.ngOnDestroy();
    discardPeriodicTasks();
  }));

  it('deve exibir erro do backend no reenvio', () => {
    authService.reenviarConfirmacao.and.returnValue(throwError({ error: { errors: ['Falha ao reenviar.'] } }));
    component.email = 'user@test.com';

    component.reenviar();

    expect(component.listAlerts[0].msg).toBe('Falha ao reenviar.');
  });
});

