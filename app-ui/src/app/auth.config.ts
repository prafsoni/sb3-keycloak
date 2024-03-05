import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  issuer: 'http://localhost:8180/realms/sb3-keycloak',
  redirectUri: window.location.origin +
    (localStorage.getItem('useHashLocationStrategy') === 'true'
      ? '/#/index.html'
      : '/index.html'),
  clientId: 'sb3-keycloak-ui',
  responseType: 'code',
  scope: 'openid profile email offline_access',
  showDebugInformation: true,
  sessionChecksEnabled: false,
  timeoutFactor: 0.01,
  // disablePKCI: true,
  clearHashAfterLogin: true,
};
