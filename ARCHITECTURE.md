# Agent Guidelines: Hexagonal + Quarkus + OpenAPI + PostgreSQL/Flyway

Use these conventions to build/reuse services with Quarkus and hexagonal architecture. Adapt the base package to your project.

## Architecture Overview
- Domain-centric: business rules live in the domain, pure Java.
- Ports and Adapters: domain defines ports; adapters implement technology specifics.
- Inbound adapters: expose use cases (REST), map DTOs to domain types.
- Outbound adapters: persistence, messaging, and external REST integrations.
- Services: implement inbound ports, orchestrating domain logic and outbound ports.

## Layering and Packages
- Domain Core
    - Models and value objects: `..domain.model.*`
    - Ports: `..domain.port.inbound.*` (use cases), `..domain.port.outbound.*` (gateways)
    - Services: `..domain.service.*` (implements inbound ports, depends only on outbound ports)
- Inbound Adapters
    - REST controllers/filters/exceptions: `..adapter.inbound.rest.*`
    - OpenAPI-generated models: `..adapter.inbound.rest.model.*`
- Outbound Adapters
    - Database: `..adapter.outbound.database.*` (entities/mappers/repositories/adapters)
    - External REST clients: `..adapter.outbound.rest.*`
    - Messaging/WebSockets/etc.: `..adapter.outbound.*`

## Inbound (REST) via OpenAPI
- Single source of truth: `src/main/resources/META-INF/openapi.yaml`
- Swagger Codegen generates inbound DTOs/models from the spec.
- Controllers remain thin: validate/map DTOs → call domain use cases → map responses.
- Regenerate after spec updates: `mvn generate-sources`

## Database Standard: PostgreSQL + Flyway (Quarkus)
Prefer PostgreSQL and Flyway for new services (or when migrating).
- Add dependencies
    - `io.quarkus:quarkus-jdbc-postgresql`
    - `io.quarkus:quarkus-hibernate-orm` (or `quarkus-hibernate-orm-panache`)
    - `io.quarkus:quarkus-flyway`
- Configure in `application.properties`
    - `quarkus.datasource.db-kind=postgresql`
    - `quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/<db>`
    - `quarkus.datasource.username=<user>`
    - `quarkus.datasource.password=<password>`
    - `quarkus.hibernate-orm.database.generation=none`
    - `quarkus.flyway.migrate-at-start=true`
- Migrations
    - Store under `src/main/resources/db/migration` (e.g., `V1__init.sql`)
    - Use migrations as the single source of schema change
- Persistence adapters
    - Map domain ↔ JPA entities, implement outbound ports, keep mapping isolated

## Quarkus Conventions
- Keep framework imports out of domain (no JAX-RS/JPA in domain).
- Wire with CDI: `@ApplicationScoped`, `@Inject` (in adapters/services).
- Validate inbound DTOs (Bean Validation) in controllers.
- Health endpoints via SmallRye Health; metrics/logging via Quarkus extensions.

## Data Objects (Lombok)
- Use Lombok for data-holder classes (DTOs, simple models) to avoid boilerplate.
- Prefer `@Getter`/`@Setter` (or `@Data` for DTOs) and `@Builder` where helpful.
- For immutable domain types, consider `@Value` and constructors; keep domain free of framework imports.
- JPA entities (adapter layer) can use Lombok for accessors; remember a no-args constructor for JPA.
- Ensure `org.projectlombok:lombok` is added in `pom.xml` with annotation processing enabled.

## Mapping Between Layers (MapStruct)
- Purpose: centralize object mapping to keep controllers/services thin and domain clean.
- Library: MapStruct with Jakarta component model to enable CDI injection.
- Conventions
    - Inbound mapping: `..adapter.inbound.rest.mapper.*` maps OpenAPI DTOs ↔ domain models.
    - Outbound mapping: `..adapter.outbound.database.mapper.*` maps domain ↔ persistence entities/documents.
    - Use `@Mapper(componentModel = JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)`.
    - Prefer explicit `@Mapping` for differing field names and nested conversions.
    - Share reusable conversions via `@Named` methods; compose mappers using `uses = {OtherMapper.class}`.
- Example skeleton
    - `@Mapper(componentModel = JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
    interface RestInboundObjMapper { DomainObj toDomain(InboundDto dto); InboundDto fromDomain(DomainObj domain); }`
    - Place mapping-related cross-cutting utilities in `..mapper.common.*`.

## Naming
- Ports: `*UseCase` (inbound) and `*Port` (outbound)
- Adapters: `*Adapter`, repositories: `*Repository`, mappers: `*Mapper`
- Controllers suffix: `*Controller`

## Feature Workflow (Checklist)
1. Extend domain model/types as needed.
2. Add inbound port in `domain.port.inbound`.
3. Implement service in `domain.service`, inject outbound ports.
4. Update `openapi.yaml` if API changes; `mvn generate-sources`.
5. Add REST controller calling the use case; DTO ↔ domain mapping.
6. Add outbound ports for persistence/integrations.
7. Implement outbound adapters (DB/REST/messaging) and required mappers/entities.
8. Add Flyway migration(s) for schema changes.
9. Add tests at controller/service boundaries.

## Build & Run
- Dev mode: `mvn quarkus:dev`
- Tests: `mvn test`
- Regenerate OpenAPI models: `mvn generate-sources`