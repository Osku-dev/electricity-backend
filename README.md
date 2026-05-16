# Electricity Price Backend

Java Spring Boot backend for a [Finnish electricity spot price application](https://github.com/Osku-dev/electricity-price-app).

The backend fetches and serves electricity spot price data and exposes it via a GraphQL API.  
GraphQL was chosen mainly for learning purposes and to experiment with flexible querying on the client side.

The project is structured to support:
- Current spot price queries
- Historical price data
- Cursor-based pagination for historical prices (not yet used by the app, but ready)

This allows the mobile app to later add features like browsing older price data without major backend changes.

## Tech stack

- Java
- Spring Boot
- GraphQL
- PostgreSQL
- Flyway

## CI/CD (GitHub Actions + Render)

- CI runs on every pull request and non-main push via `.github/workflows/ci.yml`
- CI uses Java 17 and runs `./mvnw -B verify`
- CD runs on `main` pushes via `.github/workflows/render-deploy.yml`
- CD triggers a Render deploy hook using the GitHub secret `RENDER_DEPLOY_HOOK_URL`

### Render setup

1. Create a new **Web Service** in Render from this repo (native build is enough, Docker is optional).
2. Set build command to `./mvnw -DskipTests package`.
3. Set start command to `java -jar target/electricity-backend-0.0.1-SNAPSHOT.jar`.
4. Provision a Render PostgreSQL database and copy its internal connection details.
5. Add required environment variables in Render (for example: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`, `DAILY_PRICES_URL`, `HOURLY_PRICE_URL`, `FRONTEND_URL`).
6. In Render service settings, create a **Deploy Hook** and save its URL to GitHub Actions secret `RENDER_DEPLOY_HOOK_URL`.
