import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { CadastroSolicitacaoComponent } from './login/cadastro-solicitacao.component';
import { ConfirmarCadastroComponent } from './login/confirmar-cadastro.component';
import { LayoutComponent } from './layout/layout.component';
import { AuthGuard } from './auth.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'cadastro', component: CadastroSolicitacaoComponent },
  { path: 'confirmar-cadastro', component: ConfirmarCadastroComponent },
  { path: '', component: LayoutComponent, children: [
    { path : 'home', component: HomeComponent, canActivate : [AuthGuard] },
    { path: '' , redirectTo: '/home', pathMatch: 'full' }
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
