// Keep your existing OIDC configuration. This reference configuration uses
// angular-auth-oidc-client v17 and the Auth Service running on localhost:8085.
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAuth, authInterceptor } from 'angular-auth-oidc-client';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor()])),
    provideAuth({
      config: {
        authority: 'http://localhost:8085',
        redirectUrl: 'http://localhost:4200/auth/callback',
        postLogoutRedirectUri: 'http://localhost:4200/login',
        clientId: 'retail-angular',
        scope: 'openid profile retail.read retail.write',
        responseType: 'code',
        silentRenew: false,
        useRefreshToken: false,
        secureRoutes: ['http://localhost:8080']
      }
    })
  ]
};
