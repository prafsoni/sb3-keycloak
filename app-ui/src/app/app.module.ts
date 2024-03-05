import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { OAuthModule } from 'angular-oauth2-oidc';

import { AppComponent } from './app.component';
import {SharedModule} from "./shared/shared.module";
import {RouterModule} from "@angular/router";
import {APP_ROUTES} from "./app.routes";
import {useHash} from "../flags";
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(APP_ROUTES, { useHash }),
    HttpClientModule,
    SharedModule.forRoot(),
    OAuthModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
