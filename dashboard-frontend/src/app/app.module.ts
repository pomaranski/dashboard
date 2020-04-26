import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HeaderComponent } from './core/layouts/header/header.component';
import { FooterComponent } from './core/layouts/footer/footer.component';
import { MainComponent } from './core/layouts/main/main.component';
import { HomePageComponent } from './features/home-page/home-page.component';
import { OwnerApiService } from './core/services/api/owner-api.service';
import { AppRoutingModule } from './app-routing.module';

const LAYOUTS = [
  HeaderComponent,
  FooterComponent,
  MainComponent,
];

const PAGES = [
  HomePageComponent,
];

const API_SERVICES = [
  OwnerApiService,
];

@NgModule({
  declarations: [
    AppComponent,
    ...LAYOUTS,
    ...PAGES,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    ...API_SERVICES,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
