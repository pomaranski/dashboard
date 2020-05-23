import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from './core/guards/auth.guard';
import { MainComponent } from './core/layouts/main/main.component';
import { LoginPageComponent } from './features/login-page/login-page.component';
import { HomePageComponent } from './features/home-page/home-page.component';
import { AddInstancePageComponent } from './features/instances/add-instance-page/add-instance-page.component';
import { InstancePageComponent } from './features/instances/instance-page/instance-page.component';

const routes: Routes = [
    {
        path: '',
        component: MainComponent,
        children: [
            {
                path: 'login',
                component: LoginPageComponent,
            },
            {
                path: 'home',
                canActivate: [AuthGuard],
                component: HomePageComponent,
            },
            {
                path: 'instances/add',
                canActivate: [AuthGuard],
                component: AddInstancePageComponent,
            },
            {
                path: 'instances',
                canActivate: [AuthGuard],
                component: InstancePageComponent,
            },
            {
                path: '**',
                redirectTo: '',
            },
        ],
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
exports: [RouterModule],
})
export class AppRoutingModule {}
