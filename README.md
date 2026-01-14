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
