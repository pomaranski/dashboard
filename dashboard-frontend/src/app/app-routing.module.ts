import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MainComponent } from './core/layouts/main/main.component';
import { HomePageComponent } from './features/home-page/home-page.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    children: [
      {
        path: 'home',
        component: HomePageComponent,
      },
      {
        path: '**',
        pathMatch: 'full',
        redirectTo: 'home',
      },
    ],
  }
];

@NgModule({
  imports: [
  RouterModule.forRoot(routes),
  ],
  exports: [
    RouterModule,
  ],
})
export class AppRoutingModule { }
