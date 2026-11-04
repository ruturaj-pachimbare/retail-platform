# Config Server

Java 21, Spring Boot 3.5.7, Spring Cloud Config Server 2025.0.0.

## Folder layout

Extract the ZIP into `retail-platform/backend/`. It creates sibling folders:

- `backend/config-server`
- `backend/config-repo`

The default Git backend URI is `file:../config-repo`, resolved while running from `backend/config-server`.

## 1. Initialize the local configuration Git repository

```powershell
cd D:\Ruturaj\CompleteProject\retail-platform\backend\config-repo
git init -b main
git add .
git commit -m "chore: add centralized microservice configurations"
```

If it is already a Git repository, do not initialize it again. Run `git status` and commit the files if needed.

## 2. Start Config Server

```powershell
cd D:\Ruturaj\CompleteProject\retail-platform\backend\config-server
mvn spring-boot:run
```

Wait for `Started ConfigServerApplication`.

## 3. Verify

- http://localhost:8888/actuator/health
- http://localhost:8888/product-service/default
- http://localhost:8888/inventory-service/default
- http://localhost:8888/order-service/default
- http://localhost:8888/notification-service/default

A successful config response contains `propertySources`.

## Remote Git repository later

When a dedicated private GitHub config repository is ready, set `CONFIG_GIT_URI` to its HTTPS URL. Configure credentials securely through environment variables or a secret manager. Never commit passwords, tokens, or client secrets.

## Scope

This bundle creates Config Server and starter config files. The four existing business services are not yet Config Clients; we will migrate their complete configurations in a later integration step.
