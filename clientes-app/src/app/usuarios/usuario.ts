import { InfoResponse } from '../infoResponse';

export class Usuario {
  id: number;
  username: string;
  email: string;
  password: string;
  cpf: string;
  telefone: string;
  endereco: string;
  cidade: string;
  uf: string;
  cep: string;
  ativo = true;
  emailConfirmed: boolean;
  roles: string[] = [];
  infoResponseDTO?: InfoResponse;
}
