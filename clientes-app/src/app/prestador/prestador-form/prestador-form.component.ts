import { ValidadorService } from './../../validador.service';
import { ProfissaoService } from './../../profissao.service';
import { PrestadorService } from './../../prestador.service';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import GoogleCaptchaService from 'src/app/google-captcha.service';
import { GoogleCaptcha } from 'src/app/googleCaptcha';
import { NotificationService } from '../../notification.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Prestador } from '../prestador';
import { Observable } from 'rxjs';
import { Profissao } from 'src/app/profissao';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { Constants } from 'src/app/shared/constants';
import { Alert } from 'src/app/alert';
import { MaskUtil } from 'src/app/shared/utils/mask.util';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-prestador-form',
  templateUrl: './prestador-form.component.html',
  styleUrls: ['./prestador-form.component.css']
})
export class PrestadorFormComponent implements OnInit, OnDestroy {
  prestador: Prestador;
  success: boolean = false;
  errors: string[] = [];
  id: number = 0;
  captcha: string;
  googlecaptcha: GoogleCaptcha | undefined;
  profissao: Profissao[] = [];
  valorValido: string = "0";

  max = 10;
  isReadonly = false;
  dropdownSettings: IDropdownSettings = {};
  profissaoSelecionada = [];

  foto!: SafeUrl | string;
  filename: string = "";
  files: any[] = [];
  @ViewChild('inputFile', { static: true }) inputFile!: ElementRef;
  fileList: File[] = [];
  originalFileName: string = "";
  fotoNotFound: string = Constants.fotoNotFound;
  private currentObjectUrl: string | null = null;
  TimeOut = Constants.TIMEOUT;
  listAlerts: Alert[] = [];

  constructor(
    private service: PrestadorService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    // private notificationService: NotificationService,
    private googleCaptchaService: GoogleCaptchaService,
    private profissaoService: ProfissaoService,
    private validadorService: ValidadorService,
    private sanitizer: DomSanitizer
  ) {
    this.prestador = new Prestador();
    this.prestador.profissao = new Profissao();
    this.captcha = "";
   }

  ngOnInit(): void {
    let params : Observable<Params> = this.activatedRoute.params;
    params.subscribe( urlParams => {
      this.id = urlParams['id'];
      if (this.id) {
        this.service
          .obterPorId(this.id)
          .subscribe(
            response => {
                this.prestador = response,
                this.aplicarMascarasPrestador(),
                this.profissaoService.obterProfissaoPorId(this.prestador.profissao.id)
                  .subscribe(resposta => {
                     this.prestador.profissao.id = resposta.id;
                     this.profissaoSelecionada = [{ id: resposta.id,
                          descricao: resposta.descricao
                        }];

                  })
            },
            errorResponse => this.prestador = new Prestador()
          );
          this.obterImagem(this.id);
      } else {
        this.prestador.nome = "";
      }
  });

    if (this.prestador.avaliacao == undefined) {
      this.prestador.avaliacao = 0;
    }

    this.dropdownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'descricao',
      selectAllText: 'Selecionar Tudo',
      unSelectAllText: 'Tirar seleção de Tudo',
      searchPlaceholderText: 'Procurar',
      noDataAvailablePlaceholderText: 'Nenhum Registro Encontrado',
      closeDropDownOnSelection: true,
      maxHeight: 300,
      itemsShowLimit: 3,
      allowSearchFilter: true,
      allowRemoteDataSearch: true
    };
  }

  confirmSelection(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.isReadonly = true;
    }
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
          this.foto = reader.result as string;
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
  if (this.fileList != undefined && this.fileList.length && this.prestador?.id) {
    const item: File = this.fileList[0];
    this.service.uploadFoto(this.prestador.id, item)
      .subscribe(() => {
        this.obterImagem(this.prestador.id);
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

      this.service.deletarFoto(this.prestador.id)
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

  resetStars($event: Event) {
    this.isReadonly = !this.isReadonly;
    this.prestador.avaliacao = 0;
    $event.preventDefault();
  }

  confirmClickRating($event: Event) {
    $event.preventDefault();
  }

  voltarParaListagem(){
    this.router.navigate(['/prestador/lista']);
  }

  resolved(captchaResponse: string) {
    this.captcha = captchaResponse;
    this.googleCaptchaService.verificar(this.captcha)
      .subscribe(response => {
        this.success = true;
        this.errors = null!;
        this.googlecaptcha = response;
      }, errorResponse => {
        this.success = false;
        this.errors = errorResponse.error.errors;
        this.errors.forEach( (erro) =>{
         // this.notificationService.showToasterError(erro, "erro");
        })
      })
    }

  validaCampoProfissao(){
    if (!this.prestador.profissao) {
      this.prestador.profissao = new Profissao();
    }

    const profissaoSelecionada = this.profissaoSelecionada[0];
    this.prestador.profissao.id = profissaoSelecionada.id;
    this.prestador.idProfissao = profissaoSelecionada.id;
  }

    onSubmit(){
      this.prestador.captcha = this.captcha;
      if (this.profissaoSelecionada[0] == undefined) {
          // this.notificationService.showToasterError("Favor selecionar uma Profissão", "Erro");
          this.listAlerts.push({
            "msg": "Favor selecionar uma Profissão",
            "timeout": this.TimeOut,
            "type": "danger"
          });
          return;
      }

      this.validaCampoProfissao();
      const payload = this.buildPrestadorPayload();

      if (this.id) {
          this.service
            .atualizar(payload)
            .subscribe(response => {
                  this.success = true;
                // this.router.navigate(['/prestador/lista']);
                this.router.navigate(['/prestador/lista'], { state: {mensagem: response.mensagem }});
                // this.notificationService.showToasterSuccessWithTitle(response.mensagem,
                //  response.titulo);

                this.errors = null || [];
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
            .subscribe(response => {
              this.prestador.id = response.id;
              this.salvarImagem();
              this.success = true;
              // this.router.navigate(['/prestador/lista']);
              this.router.navigate(['/prestador/lista'], { state: {mensagem: response.infoResponseDTO.mensagem }});
              //  this.notificationService.showToasterSuccessWithTitle(response.infoResponseDTO.mensagem,
              //  response.infoResponseDTO.titulo);
              this.errors = null;
              this.prestador = new Prestador();
              this.prestador.profissao = new Profissao();
            } , errorResponse => {
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
      this.prestador.cpf = MaskUtil.applyCpf(value);
    }

    private aplicarMascarasPrestador(): void {
      this.prestador.cpf = MaskUtil.applyCpf(this.prestador.cpf);
    }

    private buildPrestadorPayload(): Prestador {
      return {
        ...this.prestador,
        cpf: MaskUtil.digitsOnly(this.prestador.cpf),
        idProfissao: this.prestador.idProfissao,
        profissao: {
          ...this.prestador.profissao,
          id: this.prestador.idProfissao
        }
      };
    }

    apagar() {
      this.prestador.avaliacao = 0;
      this.prestador.cpf = "";
      this.prestador.nome = "";
      this.prestador.pix = "";
      this.prestador.dataCadastro = "";
      this.prestador.profissao = new Profissao();
      this.prestador.email = "";
      this.limparProfissao();
    }

    onItemSelect(item: any) {
      this.prestador.profissao.id = item.id;
    }

    onItemDeSelect(item: any){
      this.limparProfissao();
    }

    onSelectAll(items: any) {
      console.log('onSelectAll: ' + items);
    }

    onFilterChange(item: any){
      if (item.length < 3) {
        this.profissao = [];
      }
      else {
        this.profissaoService
        .obterProfissoes(item)
        .subscribe(resposta => {
          this.profissao = resposta;
        });
      }
    }

    limparProfissao() {
      this.profissaoSelecionada = [];
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
