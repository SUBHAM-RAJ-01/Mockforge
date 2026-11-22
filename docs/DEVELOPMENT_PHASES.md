# MockForge Development Phases

## Overview

This document outlines the phased development approach for MockForge. Each phase is designed to deliver working, testable functionality that can be deployed and evaluated independently.

**Development Timeline**: 10-12 weeks (solo developer)

---

## Phase 0: Foundation & Technical Spike
**Duration**: 2 weeks (10 working days)  
**Status**: 🟢 IN PROGRESS  
**Start Date**: November 22, 2026

### Goals
Validate core technical decisions and establish development infrastructure before building full features.

### Technical Spikes
1. ✅ Dynamic CRUD routing without one controller per resource
2. ✅ Hybrid storage semantics (SQLite + in-memory)
3. ✅ Atomic runtime snapshot replacement
4. ✅ Docker volume persistence on Windows/macOS/Linux

### Deliverables
- [x] Repository structure and conventions
- [x] Documentation framework
- [x] Development environment setup
- [ ] Spring Boot skeleton with SQLite connection
- [ ] Simple route matcher POC
- [ ] In-memory and SQLite DataStore interfaces
- [ ] Docker build with multi-stage compilation
- [ ] Docker Compose with volume persistence test
- [ ] Basic CI/CD pipeline (GitHub Actions)

### Exit Criteria
- [ ] A single dynamic resource route works end-to-end
- [ ] Data persists in SQLite after container restart
- [ ] Docker volume works on test platforms (Linux minimum)
- [ ] Build and test pipeline is green

### Days Breakdown
| Days | Work |
|------|------|
| 1-2  | Repo setup, Java 21, Spring Boot, SQLite config, migration tool evaluation |
| 3-4  | Project/resource metadata entities and management endpoints |
| 5-6  | Route matcher POC and dynamic CRUD handler skeleton |
| 7-8  | In-memory and SQLite DataStore implementations |
| 9-10 | Hybrid write-through semantics, Docker volume test, documentation |

---

## Phase 1: Core Engine
**Duration**: 2 weeks  
**Status**: ⚪ PLANNED  
**Dependencies**: Phase 0 complete

### Goals
Build the foundational management plane and runtime engine with basic CRUD operations.

### Features
- Project CRUD (create, read, update, delete, list)
- Resource schema management (fields, types, constraints)
- Dynamic CRUD route generation
- Memory + SQLite storage modes
- Basic field validation
- Simple filtering and pagination

### Deliverables
- [ ] Complete Project management APIs
- [ ] Resource schema builder (backend)
- [ ] Compiler: schema → RuntimeSnapshot
- [ ] Collection, single record, create, update, delete handlers
- [ ] Query parser for basic filters
- [ ] Unit tests for all handlers
- [ ] Integration tests with TestContainers

### Exit Criteria
- [ ] Can create a project via API
- [ ] Can define a resource with 5+ field types
- [ ] Can POST/GET/PUT/PATCH/DELETE records
- [ ] Records persist in SQLite mode
- [ ] Memory mode works without SQLite
- [ ] Query `?page=1&limit=10` works correctly

### API Examples
```bash
# Create project
POST /_mockforge/v1/projects
{
  "name": "Test Project",
  "basePath": "/api",
  "storageMode": "PERSISTENT"
}

# Create resource
POST /_mockforge/v1/projects/{id}/resources
{
  "name": "User",
  "slug": "users",
  "fields": [
    {"name": "name", "type": "STRING", "required": true},
    {"name": "email", "type": "EMAIL", "unique": true}
  ]
}

# Publish (compile snapshot)
POST /_mockforge/v1/projects/{id}/publish

# Call generated API
GET /api/users
POST /api/users {"name": "Alice", "email": "alice@example.com"}
```

---

## Phase 2: Dashboard UI
**Duration**: 2 weeks  
**Status**: ⚪ PLANNED  
**Dependencies**: Phase 1 complete

### Goals
Build the React dashboard for managing projects, resources, and data without manually editing JSON.

### Features
- Project workspace (create, select, overview)
- Resource schema builder UI
- Data manager (table view, JSON editor)
- Basic record CRUD operations
- Field type selector and constraint editor
- Publish button with validation feedback

### Deliverables
- [ ] React app structure with routing
- [ ] TypeScript API client layer
- [ ] Project list and detail screens
- [ ] Resource schema builder form
- [ ] Data table with inline editing
- [ ] JSON import/export UI
- [ ] Error handling and loading states

### Exit Criteria
- [ ] Can create and configure a project entirely from UI
- [ ] Can define a resource with 5 fields
- [ ] Can add, edit, delete records in table view
- [ ] Validation errors display clearly
- [ ] Publish feedback shows success or errors

---

## Phase 3: Query & Relationships
**Duration**: 2 weeks  
**Status**: ⚪ PLANNED  
**Dependencies**: Phase 2 complete

### Goals
Add advanced query capabilities and resource relationships.

### Features
- Filtering (eq, ne, gt, lt, gte, lte, contains)
- Sorting (single and multi-field)
- Pagination with metadata
- One-to-many relationships
- Many-to-one relationships
- Relationship resolution in responses

### Deliverables
- [ ] Query parser with operator support
- [ ] QueryEngine for in-memory and SQL filtering
- [ ] Relationship metadata in schema
- [ ] Relationship handler (e.g., `/users/{id}/orders`)
- [ ] UI for defining relationships
- [ ] Nested/expanded response support

### Exit Criteria
- [ ] `GET /users?age_gte=18&sortBy=name&order=asc` works
- [ ] Can define User has-many Orders
- [ ] `GET /users/{id}/orders` returns related records
- [ ] Frontend displays related records correctly

---

## Phase 4: Custom Endpoints
**Duration**: 1.5 weeks  
**Status**: ⚪ PLANNED  
**Dependencies**: Phase 3 complete

### Goals
Allow developers to define custom routes beyond auto-generated CRUD.

### Features
- Custom endpoint definitions (method, path, handler)
- Static JSON response templates
- Conditional response configuration
- Route priority and collision detection
- Status code and header configuration

### Deliverables
- [ ] Endpoint management APIs
- [ ] CustomResponseHandler
- [ ] Template evaluator (safe, no arbitrary code)
- [ ] Route validation at publish time
- [ ] UI for endpoint editor and test panel

### Exit Criteria
- [ ] Can create `POST /api/auth/login` with custom 200 response
- [ ] Can configure `GET /api/checkout` to return 402
- [ ] Route collisions are rejected at publish
- [ ] Test panel can send request and show response

---

## Phase 5: Simulation & Scenarios
**Duration**: 1.5 weeks  
**Status**: ⚪ PLANNED  
**Dependencies**: Phase 4 complete

### Goals
Enable backend behavior simulation for testing edge cases.

### Features
- Named scenarios (default, empty-state, server-error, etc.)
- Response delay configuration
- Failure injection (probability, status code)
- Rate limiting simulation
- Scenario switcher in UI

### Deliverables
- [ ] Scenario management APIs
- [ ] SimulationEngine and RuleEvaluator
- [ ] Delay and failure handlers
- [ ] Scenario selection (header or active scenario)
- [ ] UI for scenario configuration

### Exit Criteria
- [ ] Can create "slow-network" scenario with 500ms delay
- [ ] Can create "server-error" with 10% failure rate → 500
- [ ] Switching scenarios changes behavior
- [ ] Seeded randomness is reproducible

---

## Phase 6: Request Inspector
**Duration**: 1 week  
**Status**: ⚪ PLANNED  
**Dependencies**: Phase 5 complete

### Goals
Provide visibility into all requests made to mock APIs.

### Features
- Request history with metadata
- Request/response detail view
- Filtering and search
- Replay request capability
- Retention policy configuration

### Deliverables
- [ ] RequestLogService with bounded queue
- [ ] Request event capture (non-blocking)
- [ ] Inspector APIs (list, detail, replay)
- [ ] UI timeline and detail panel
- [ ] Body capture (opt-in) and redaction

### Exit Criteria
- [ ] All requests appear in inspector
- [ ] Can view headers, query, body, response, latency
- [ ] Replay button re-sends request
- [ ] Queue overflow does not stall runtime

---

## Phase 7: CLI & Packaging
**Duration**: 1 week  
**Status**: ⚪ PLANNED  
**Dependencies**: Phase 6 complete

### Goals
Deliver complete Docker packaging and optional CLI for project management.

### Features
- Final Docker Compose configuration
- CLI tool (Picocli) for init, start, stop, status
- Project import/export (JSON bundles)
- OpenAPI export
- Quickstart documentation

### Deliverables
- [ ] Multi-stage Dockerfile (optimized)
- [ ] Docker Compose with health checks
- [ ] CLI commands: `init`, `start`, `stop`, `import`, `export`
- [ ] OpenAPI 3.0 generator
- [ ] Complete user and developer documentation

### Exit Criteria
- [ ] `docker compose up` starts entire stack
- [ ] CLI can export project as `.mockforge.json`
- [ ] Can import exported project on another machine
- [ ] OpenAPI spec matches generated routes
- [ ] Fresh user can complete quickstart in <5 minutes

---

## Phase 8: Hardening & Performance
**Duration**: 1-2 weeks  
**Status**: ⚪ PLANNED  
**Dependencies**: Phase 7 complete

### Goals
Security, concurrency, testing, and performance validation before v1 release.

### Activities
- Security audit (CORS, admin protection, input validation)
- Concurrency testing (race conditions, optimistic locking)
- SQLite migration strategy and backup
- Performance benchmarks (documented methodology)
- End-to-end test suite (Playwright or similar)
- Load testing with k6
- Documentation review and updates

### Deliverables
- [ ] Security hardening (all items in spec § 13)
- [ ] Concurrency tests pass
- [ ] Benchmark results published with methodology
- [ ] E2E test suite covers critical paths
- [ ] All "Critical Acceptance Tests" pass (spec § 14.3)

### Exit Criteria
- [ ] No high-severity security issues
- [ ] Concurrent writes handled correctly (409 on conflict)
- [ ] Publish during traffic does not corrupt requests
- [ ] Performance baselines documented
- [ ] Acceptance suite is green
- [ ] Docker restart retains persistent data

---

## Definition of Done (v1 Complete)

✅ All 8 phases completed  
✅ Can create project and resource through dashboard  
✅ Dynamic CRUD routes work for any resource  
✅ Three storage modes behave as documented  
✅ Filtering, sorting, pagination have consistent semantics  
✅ Custom endpoints return configured responses  
✅ At least three named scenarios are reproducible  
✅ Inspector displays request metadata  
✅ Project export/import works  
✅ Docker Compose quickstart is documented  
✅ Security defaults and limitations are documented  
✅ Benchmark methodology is published  

---

## Future Phases (Post-v1)

### Phase 9: Advanced Features
- GraphQL mocking
- WebSocket simulation
- Authentication/authorization simulation
- Advanced templating with placeholders

### Phase 10: Cloud & Collaboration
- Team sharing
- Cloud synchronization
- Multi-environment support
- Native CLI (GraalVM native-image)

---

## Notes

- Each phase should result in a **working, demonstrable feature**
- Commit frequently with meaningful messages
- Update this document as phases complete
- Document blockers and decisions in `docs/DECISIONS.md`
- Keep `docs/COMMIT_HISTORY.md` updated weekly

---

**Last Updated**: November 22, 2026  
**Current Phase**: Phase 0 - Foundation & Technical Spike  
**Next Review**: End of Phase 0 (Week 2)
