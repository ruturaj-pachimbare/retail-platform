
import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-auth-callback',
  standalone: true,
  template: '<p>Completing authentication...</p>'
})
export class AuthCallbackComponent implements OnInit {

  private oidcSecurityService = inject(OidcSecurityService);
  private router = inject(Router);

  ngOnInit(): void {
    this.oidcSecurityService.checkAuth().subscribe({
      next: result => {
        if (result.isAuthenticated) {
          this.router.navigate(['/dashboard']);
        } else {
          this.router.navigate(['/login']);
        }
      },
      error: error => {
        console.error('Authentication callback failed:', error);
        this.router.navigate(['/login']);
      }
    });
  }
}
