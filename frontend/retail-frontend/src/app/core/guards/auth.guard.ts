import { Injectable, inject } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable, map } from 'rxjs';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  private readonly oidcService = inject(OidcSecurityService);
  private readonly router = inject(Router);

  canActivate(): Observable<boolean | UrlTree> {
    return this.oidcService.isAuthenticated$.pipe(
      map(({ isAuthenticated }) => {
        if (isAuthenticated) {
          return true;
        }

        this.oidcService.authorize();
        return false;
      })
    );
  }
}
