import { Cidade } from './../../cidade';
import { UF } from './../../uf';
import { CepService } from './../../cep.service';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router'

import { Cliente } from '../cliente'
import { ClientesService } from '../../clientes.service'
import { empty, fromEvent, Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map, switchMap, tap } from 'rxjs/operators';
import { Endereco } from 'src/app/endereco';
import GoogleCaptchaService from 'src/app/google-captcha.service';
import { GoogleCaptcha } from 'src/app/googleCaptcha';
import { Constants } from 'src/app/shared/constants';
import { Alert } from 'src/app/alert';
import { state } from '@angular/animations';
import { MaskUtil } from 'src/app/shared/utils/mask.util';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-clientes-form',
  templateUrl: './clientes-form.component.html',
  styleUrls: ['./clientes-form.component.css']
})
export class ClientesFormComponent implements OnInit, OnDestroy {

  cliente: Cliente;
  success: boolean = false;
  errors: string[];
  id: number;
  cepPesquisado: string = "";
  endereco: Endereco = null;
  @ViewChild('cep', {static: true}) cep: ElementRef;
  @ViewChild('inputUf', {static: true}) inputUf: ElementRef;
  @ViewChild('inputCidade', {static: true}) inputCidade: ElementRef;
  uf: UF[];
  cidade: Cidade[];
  cidadeDesabilitado: boolean;
  captcha: string;
  googlecaptcha: GoogleCaptcha;
  recaptchaSiteKey = environment.recaptchaSiteKey;

  foto: SafeUrl | string;
  filename: string = "";
  files: any[] = [];
  @ViewChild('inputFile', { static: true }) inputFile: ElementRef;
  fileList: File[] = [];
  originalFileName: string = "";
  fotoNotFound: string = Constants.fotoNotFound;
  private currentObjectUrl: string | null = null;
  TimeOut = Constants.TIMEOUT;
  listAlerts: Alert[] = [];

  constructor(
      private service: ClientesService ,
      private router: Router,
      private activatedRoute: ActivatedRoute,
      // private notificationService: NotificationService,
      private cepService: CepService,
      private googleCaptchaService: GoogleCaptchaService,
      private sanitizer: DomSanitizer
      ) {
    this.cliente = new Cliente();
    this.cliente.nome = "";
    this.cliente.cpf = "";
    this.cidadeDesabilitado = false;
    this.captcha = "";
  }

  ngOnInit(): void {
    let params : Observable<Params> = this.activatedRoute.params;
    params.subscribe( urlParams => {
        this.id = urlParams['id'];
        if (this.id) {
          this.service
            .getClienteById(this.id)
            .subscribe(
              response => {
                  this.cliente = response,
                  this.aplicarMascarasCliente(),
                  this.cepService.obterCidadesNome(this.cliente.cidade)
                 .subscribe(dados => {
                    this.cidade = dados,
                    this.cidadeDesabilitado = true;
                 })
              },
              errorResponse => this.cliente = new Cliente()
            );
            this.obterImagem(this.id);
        } else {
          this.cliente.uf = "";
          this.cliente.cidade = "";
        }
    });

    fromEvent(this.cep.nativeElement, 'keyup').pipe(
      map((event: any) => {
        return MaskUtil.digitsOnly(event.target.value);
      })
      ,filter(res => res.length == 8)
      ,debounceTime(500)
      ,distinctUntilChanged()
    ).subscribe(cep => {
      this.cidadeDesabilitado = false;
      this.cepService.pesquisarCEP(cep).subscribe(response => {
        this.endereco = response;
        this.cliente.endereco = this.endereco.logradouro + " " + this.endereco.bairro;
        this.cliente.complemento = this.endereco.complemento;
        this.cliente.uf = this.endereco.uf;
        this.cliente.cidade = this.endereco.localidade;
        this.cepService.obterCidadesNome(this.cliente.cidade)
                 .subscribe(dados => {
                    this.cidade = dados,
                    this.cidadeDesabilitado = true;
                 })
      });
    });

    this.cepService.obterUF()
      .subscribe(dados => this.uf = dados);

   fromEvent(this.inputUf.nativeElement, 'change').pipe(
        tap(estado => console.log('Novo estado: ', this.cliente.uf)),
        map(estado => this.uf.filter(e => e.sigla === this.cliente.uf)),
        map(estados => estados && estados.length > 0 ? estados[0].id : empty()),
        switchMap((estadoId: number) => this.cepService.obterCidades(estadoId))
      )
      .subscribe(cidades => {
          this.cidade = cidades,
          this.cidadeDesabilitado = false });

     this.googleCaptchaService.zerarTentativasMalSucedidas()
        .subscribe(response => {});
  }

  obterImagem(idChave: number = 0) {
    this.foto = this.fotoNotFound;
    this.filename = "";
    this.originalFileName = "";
    if (idChave != undefined) {
      this.service.obterFoto(idChave).subscribe({
        next: (blob: Blob) => {
          this.setFotoPreview(blob);
          this.filename = 'Foto atual';
        },
        error: () => {
          this.foto = this.fotoNotFound;
        }
      });
    }
   }

 onFileChanged(event: any) {
    this.files = event.target.files;
  }

 selectFile(event: any) {
    if (event.target.files.length > 0) {
        const selectedFile = event.target.files[0];
        this.fileList = [selectedFile];
        this.filename = selectedFile;
    this.releaseObjectUrl();

        const reader = new FileReader();
        reader.onload = () => {
          this.foto = reader.result;
        };
        reader.readAsDataURL(selectedFile);
    }
   if (this.id && this.filename != "") { // se for alteracao, salva apenas quando ja existe id.
       this.alterarImagem();
    }
 }

 getSelectedFileName(): string {
   const file = this.filename as any;
   if (file && file.name) {
    return file.name;
   }

   if (typeof this.filename === 'string' && this.filename.trim() !== '') {
    return this.filename;
   }

   return this.originalFileName || 'Nenhum arquivo selecionado';
 }

 deleteFile(index: number, nome: any) {
    // remover item do array File[] FileList
    this.fileList.splice(index, 1);
 }

 salvarImagem() {
  if (this.fileList != undefined && this.fileList.length && this.cliente?.id) {
    const item: File = this.fileList[0];
    this.service.uploadFoto(this.cliente.id, item)
      .subscribe(() => {
        this.obterImagem(this.cliente.id);
      });
  }
}

alterarImagem() {
  if (!this.id) {
    return;
  }

  this.salvarImagem();
}

deletarImagem() {
      if (!this.id) {
        return;
      }

      this.service.deletarFoto(this.cliente.id)
      .subscribe({
        next: () => {
          this.foto = this.fotoNotFound;
          this.filename = "";
          this.fileList = [];
        },
        error: () => {
          this.foto = this.fotoNotFound;
        }
      });
 }

  voltarParaListagem(){
    this.router.navigate(['/clientes/lista'])
  }

  onSubmit(){
    this.cliente.captcha = this.captcha;
    const payload = this.buildClientePayload();

    if (this.id) {
      this.service
        .atualizar(payload)
        .subscribe(response => {
            this.success = true;
            this.router.navigate(['/clientes/lista'], { state: {mensagem: response.mensagem }});
            //this.notificationService.showToasterSuccess("Cliente atualizado com sucesso!");
            // this.notificationService.showToasterSuccessWithTitle(response.mensagem,
            //  response.titulo);
            /*  this.listAlerts.push({
                "msg": response.mensagem,
                "timeout": this.TimeOut,
                "type": "success"
              }); */
            this.errors = null;
        }, errorResponse => {
          this.errors = errorResponse.error.errors;
            this.errors.forEach( (erro) =>{
              // this.notificationService.showToasterError(erro, "erro");
              this.listAlerts.push({
                "msg": erro,
                "timeout": this.TimeOut,
                "type": "danger"
              });
            })
        })
    } else {
      this.service
        .salvar(payload)
          .subscribe( response => {
            this.success = true;
            this.cliente.id = response.id;
            this.salvarImagem();
            this.router.navigate(['/clientes/lista']);
            this.router.navigate(['/clientes/lista'], { state: {mensagem: response.infoResponseDTO.mensagem }});
            //this.notificationService.showToasterSuccess("Cliente salvo com sucesso!");
            // this.notificationService.showToasterSuccessWithTitle(response.infoResponseDTO.mensagem,
            //  response.infoResponseDTO.titulo);
            /*  this.listAlerts.push({
                "msg": response.infoResponseDTO.mensagem,
                "timeout": this.TimeOut,
                "type": "success"
              }); */
            this.errors = null;
            this.cliente = response;
            this.aplicarMascarasCliente();
          }, errorResponse => {
            this.success = false;
            this.errors = errorResponse.error.errors;
            this.errors.forEach( (erro) =>{
              // this.notificationService.showToasterError(erro, "erro");
              this.listAlerts.push({
                "msg": erro,
                "timeout": this.TimeOut,
                "type": "danger"
              });
            })
          })
    }
  }

  onCpfChange(value: string): void {
    this.cliente.cpf = MaskUtil.applyCpf(value);
  }

  onCepChange(value: string): void {
    this.cliente.cep = MaskUtil.applyCep(value);
  }

  private aplicarMascarasCliente(): void {
    this.cliente.cpf = MaskUtil.applyCpf(this.cliente.cpf);
    this.cliente.cep = MaskUtil.applyCep(this.cliente.cep);
  }

  private buildClientePayload(): Cliente {
    return {
      ...this.cliente,
      cpf: MaskUtil.digitsOnly(this.cliente.cpf),
      cep: MaskUtil.digitsOnly(this.cliente.cep)
    };
  }

   resolved(captchaResponse: string) {
    window.console.log('captchaResponse:  ' + captchaResponse);
    this.captcha = captchaResponse;
    this.googleCaptchaService.verificar(this.captcha)
      .subscribe(response => {
        this.success = true;
        this.errors = null;
        this.googlecaptcha = response;
      }, errorResponse => {
        this.success = false;
        this.errors = errorResponse.error.errors;
        this.errors.forEach( (erro) =>{
         // this.notificationService.showToasterError(erro, "erro");
        })
      })
    }

    apagar() {
      this.cliente.cep = "";
      this.cliente.cidade = "";
      this.cliente.complemento = "";
      this.cliente.cpf = "";
      this.cliente.dataCadastro = "";
      this.cliente.endereco = "";
      this.cliente.pix = "";
      this.cliente.uf = "";
      if (!this.id) {
        this.cliente.nome = "";
      }
   }

   private setFotoPreview(blob: Blob): void {
    if (!blob || blob.size === 0) {
      this.releaseObjectUrl();
      this.foto = this.fotoNotFound;
      return;
    }
    this.releaseObjectUrl();
    this.currentObjectUrl = URL.createObjectURL(blob);
    this.foto = this.sanitizer.bypassSecurityTrustUrl(this.currentObjectUrl);
  }

  ngOnDestroy(): void {
    this.releaseObjectUrl();
  }

  private releaseObjectUrl(): void {
    if (this.currentObjectUrl) {
      URL.revokeObjectURL(this.currentObjectUrl);
      this.currentObjectUrl = null;
    }
  }

}
