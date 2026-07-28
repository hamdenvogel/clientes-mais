import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AlertMessageModule } from '../alert-message/alert-message.module';
import { UsuariosRoutingModule } from './usuarios-routing.module';
import { UsuariosListaComponent } from './usuarios-lista/usuarios-lista.component';
import { UsuariosFormComponent } from './usuarios-form/usuarios-form.component';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  declarations: [
    UsuariosListaComponent,
    UsuariosFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    NgxPaginationModule,
    AlertMessageModule,
    UsuariosRoutingModule
  ],
  exports: [
    UsuariosListaComponent,
    UsuariosFormComponent
  ]
})
export class UsuariosModule { }
