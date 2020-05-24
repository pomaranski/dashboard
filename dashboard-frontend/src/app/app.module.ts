import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';    
import { ToastrModule } from 'ngx-toastr'; 

import { FontAwesomeModule, FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';

import { AppRoutingModule } from './app-routing.module';
import { JwtInterceptor } from './core/interceptors/jwt-interceptor';
import { ErrorInterceptor } from './core/interceptors/error-interceptor';
import { AppComponent } from './app.component';
import { HeaderComponent } from './core/layouts/header/header.component';
import { FooterComponent } from './core/layouts/footer/footer.component';
import { LoginPageComponent } from './features/login-page/login-page.component';
import { MainComponent } from './core/layouts/main/main.component';
import { HomePageComponent } from './features/home-page/home-page.component';
import { AddInstancePageComponent } from './features/instances/add-instance-page/add-instance-page.component';
import { InstancePageComponent } from './features/instances/instance-page/instance-page.component';
import { UnassignedInstancesComponent } from './features/instances/unassigned-instances-page/unassigned-instances-page.component';
import { ConfirmModalComponent } from './shared/components/confirm-modal/confirm-modal.component';
import { SafePipe } from './core/pipes/safe.pipe';

const LAYOUTS = [
    HeaderComponent,
    FooterComponent,
    MainComponent,
];

const PAGES = [
    LoginPageComponent,
    HomePageComponent,
    AddInstancePageComponent,
    InstancePageComponent,
    UnassignedInstancesComponent,
];

const COMPONENTS = [
    ConfirmModalComponent
];

const PIPES = [
    SafePipe
];

const INTERCEPTORS = [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
];

@NgModule({
    declarations: [
        AppComponent,
        ...LAYOUTS,
        ...PAGES,
        ...COMPONENTS,
        ...PIPES,
    ],
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        AppRoutingModule,
        HttpClientModule,
        FontAwesomeModule,
        BrowserAnimationsModule,  
        ToastrModule.forRoot()  
    ],
    providers: [
        ...INTERCEPTORS,
    ],
    bootstrap: [AppComponent],
})
export class AppModule {
    constructor(iconLibrary: FaIconLibrary) {
        iconLibrary.addIconPacks(fas);
    }
}
