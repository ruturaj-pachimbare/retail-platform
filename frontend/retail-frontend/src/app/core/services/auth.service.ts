import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { environment } from '../../../environments/environment';

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly oidc = inject(OidcSecurityService);
  private readonly http = inject(HttpClient);
  private readonly authUrl = 'http://localhost:8085/api/v1/auth';

  readonly isAuthenticated$ = this.oidc.isAuthenticated$;
  readonly userData$ = this.oidc.userData$;

  login(): void {
    this.oidc.authorize();
  }

  logout(): void {
    this.oidc.logoff().subscribe();
  }

  register(request: RegisterRequest): Observable<unknown> {
    return this.http.post(`${this.authUrl}/register`, request);
  }
}
