import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { TotalRegistros } from './total-registros';

@Injectable({
  providedIn: 'root'
})
export class EquipamentoService {

  apiURL: string = environment.apiURLBase + "/api/equipamento";

  constructor(private http: HttpClient) { }

  obterListaSemPaginacao(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiURL}/lista-sem-paginacao`);
  }

  obterPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiURL}/lista-sem-paginacao/${id}`);
  }

  total(): Observable<TotalRegistros> {
    return this.http.get<TotalRegistros>(`${this.apiURL}/total`);
  }

  obterPesquisaPaginada(page, size, descricao, idServicoPrestado): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', 'descricao,asc');

    if (descricao) {
      params.set('descricao', descricao);
    }
    if (idServicoPrestado) {
      params.set('id-servico-prestado', idServicoPrestado);
    }

    return this.http.get<any>(`${this.apiURL}/pesquisa-paginada?${params.toString()}`);
  }

  salvar(equipamento: any): Observable<any> {
    return this.http.post<any>(this.apiURL, equipamento);
  }

  atualizar(id: number, equipamento: any): Observable<any> {
    return this.http.put<any>(`${this.apiURL}/${id}`, equipamento);
  }

  deletar(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiURL}/${id}`);
  }
}
