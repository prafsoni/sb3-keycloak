import { Component } from '@angular/core';
import {OAuthService} from "angular-oauth2-oidc";
import {authConfig} from "./auth.config";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app-ui';

  constructor(private oauthService: OAuthService) {
    this.configure();
  }
  private configure() {
    this.oauthService.configure(authConfig);
    this.oauthService.loadDiscoveryDocumentAndTryLogin();
  }
}
