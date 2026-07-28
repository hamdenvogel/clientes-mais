import { FormsModule } from '@angular/forms';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of, throwError } from 'rxjs';

import { LoginComponent } from './login.component';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async(() => {
    authService = jasmine.createSpyObj('AuthService', ['tentarLogar']);
    router = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('deve navegar para o fluxo dedicado de cadastro', () => {
    const event = jasmine.createSpyObj('event', ['preventDefault']);

    component.preparaCadastrar(event as any);

    expect(event.preventDefault).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/cadastro']);
  });

  it('deve alertar quando login e senha nao forem informados', () => {
    component.username = '';
    component.password = '';

    component.onSubmit();

    expect(authService.tentarLogar).not.toHaveBeenCalled();
    expect(component.listAlerts[0].msg).toContain('Informe login e senha');
  });

  it('deve autenticar, armazenar o token e navegar para home', () => {
    spyOn(Storage.prototype, 'setItem');
    authService.tentarLogar.and.returnValue(of({ token: 'jwt-token' }));
    component.username = 'usuario';
    component.password = 'Senha@123';

    component.onSubmit();

    expect(authService.tentarLogar).toHaveBeenCalled();
    expect(Storage.prototype.setItem).toHaveBeenCalledWith('token', JSON.stringify({ token: 'jwt-token' }));
    expect(Storage.prototype.setItem).toHaveBeenCalledWith('username', 'usuario');
    expect(router.navigate).toHaveBeenCalledWith(['/home']);
  });

  it('deve exibir mensagem do backend quando o login falhar', () => {
    authService.tentarLogar.and.returnValue(throwError({ error: { errors: ['Conta nao confirmada.'] } }));
    component.username = 'usuario';
    component.password = 'Senha@123';

    component.onSubmit();

    expect(component.listAlerts[0].msg).toBe('Conta nao confirmada.');
  });
});

