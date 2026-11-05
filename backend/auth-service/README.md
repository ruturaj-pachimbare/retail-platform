# Auth Service — OAuth2/OIDC
Java 21, Spring Boot 3.5.7, Spring Authorization Server, SQL Server, Flyway, Eureka and Config Client.

## Start
1. Create an empty `auth_db` database in SSMS; reuse the existing SQL Server container.
2. Start Config Server and Eureka if available.
3. Run `mvn clean test` then `mvn spring-boot:run`.

Health: http://localhost:8085/actuator/health
OIDC discovery: http://localhost:8085/.well-known/openid-configuration
JWK set: http://localhost:8085/oauth2/jwks
Registration: POST http://localhost:8085/api/v1/auth/register

Demo users seeded for local testing:
- demo / Demo@12345 (CUSTOMER)
- admin / Admin@12345 (ADMIN)

Postman OAuth 2.0: Authorization Code (With PKCE); Auth URL `http://localhost:8085/oauth2/authorize`;
Token URL `http://localhost:8085/oauth2/token`; Client ID `retail-postman`; Client Secret
`PostmanDevSecret_ChangeMe`; callback `https://oauth.pstmn.io/v1/callback`; scopes
`openid profile retail.read retail.write`; PKCE SHA-256; client authentication Basic Auth.
Log in in the browser using the demo account and click Use Token.

Production hardening required: RegisteredClientRepository is in-memory and RSA signing key is
generated at startup, so registrations and keys do not survive restarts. Persist clients,
authorizations and consents; load a stable signing key from a protected keystore/KMS with rotation;
use HTTPS and externalized secrets. Add rate limiting, email verification, password recovery and
security monitoring. Gateway and business services must separately validate JWT access tokens;
do not use OIDC ID tokens to authorize API calls. Demo accounts and credentials are local-only.
