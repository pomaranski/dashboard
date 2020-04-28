import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { FontAwesomeModule, FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';

import { AppComponent } from './app.component';
import { HeaderComponent } from './core/layouts/header/header.component';
import { FooterComponent } from './core/layouts/footer/footer.component';
import { LoginPageComponent } from './features/login-page/login-page.component';
import { MainComponent } from './core/layouts/main/main.component';
import { HomePageComponent } from './features/home-page/home-page.component';
import { AppRoutingModule } from './app-routing.module';
import { JwtInterceptor } from './core/interceptors/jwt-interceptor';
import { ErrorInterceptor } from './core/interceptors/error-interceptor';

const LAYOUTS = [
    HeaderComponent,
    FooterComponent,
    MainComponent,
];

const PAGES = [
    LoginPageComponent,
    HomePageComponent,
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
    ],
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        AppRoutingModule,
        HttpClientModule,
        FontAwesomeModule,
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
