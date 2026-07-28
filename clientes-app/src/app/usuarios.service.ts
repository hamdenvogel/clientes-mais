import { TotalUsuarios } from './clientes/totalUsuarios';
import { InfoResponse } from './infoResponse';
import { Usuario } from './usuarios/usuario';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from './../environments/environment';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {
  apiURL: string = environment.apiURLBase + '/api/usuarios';

  constructor( private http: HttpClient) {}

  salvar(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiURL, usuario);
  }

  atualizar(usuario: Usuario): Observable<InfoResponse> {
    return this.http.put<InfoResponse>(`${this.apiURL}/${usuario.id}`, usuario);
  }

  deletar(idUsuario: number): Observable<InfoResponse> {
    return this.http.delete<InfoResponse>(`${this.apiURL}/${idUsuario}`);
  }

  obterTodos(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiURL);
  }

  obterPorId(idUsuario: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiURL}/${idUsuario}`);
  }

  listarPerfis(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiURL}/perfis`);
  }

  totalUsuarios(): Observable<TotalUsuarios>{
    return this.http.get<TotalUsuarios>(`${this.apiURL}/totalUsuarios`);
  }
}
