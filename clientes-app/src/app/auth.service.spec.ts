import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { environment } from '../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('deve enviar credenciais para signin', () => {
    service.tentarLogar({ login: 'user', senha: 'Senha@123' } as any).subscribe();

    const req = httpMock.expectOne(environment.apiURLBase + '/api/auth/signin');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ login: 'user', senha: 'Senha@123' });
    req.flush({ token: 'jwt-token' });
  });

  it('deve iniciar o cadastro por e-mail', () => {
    service.iniciarCadastro({ username: 'novo', email: 'novo@test.com' }).subscribe();

    const req = httpMock.expectOne(environment.apiURLBase + '/api/auth/registration/signup-init');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ username: 'novo', email: 'novo@test.com' });
    req.flush({ message: 'ok' });
  });

  it('deve validar o token de cadastro', () => {
    service.validarTokenCadastro('abc-token').subscribe();

    const req = httpMock.expectOne((request) =>
      request.url === environment.apiURLBase + '/api/auth/registration/validate-token'
      && request.params.get('token') === 'abc-token');
    expect(req.request.method).toBe('GET');
    req.flush({ valid: true });
  });

  it('deve concluir o cadastro', () => {
    const payload = { token: 'abc-token', password: 'Senha@123', confirmPassword: 'Senha@123' };
    service.concluirCadastro(payload).subscribe();

    const req = httpMock.expectOne(environment.apiURLBase + '/api/auth/registration/complete');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ message: 'ok' });
  });

  it('deve reenviar a confirmacao', () => {
    service.reenviarConfirmacao('user@test.com').subscribe();

    const req = httpMock.expectOne(environment.apiURLBase + '/api/auth/registration/resend-confirmation');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'user@test.com' });
    req.flush({ message: 'ok' });
  });

  it('deve obter token a partir do storage em JSON ou texto simples', () => {
    localStorage.setItem('token', JSON.stringify({ token: 'jwt-json' }));
    expect(service.obterToken()).toBe('jwt-json');

    localStorage.setItem('token', 'jwt-plain');
    expect(service.obterToken()).toBe('jwt-plain');
  });
});

