# MockForge Commit History

This document tracks major commits, decisions, and milestones throughout the project development.

## Format
```
YYYY-MM-DD | Phase | Commit Summary | Notes
```

---

## Phase 0: Foundation & Technical Spike

### Week 1: November 22-28, 2026

| Date | Commit Summary | Files Changed | Notes |
|------|---------------|---------------|-------|
| 2026-11-22 | docs: initialize project with comprehensive documentation and branding | 8 files | Created comprehensive project structure with phase-wise development plan |
| 2026-11-22 | chore(backend): initialize Spring Boot project structure | backend/pom.xml, application.yml, main class | Maven POM with Java 21, Spring Boot 3.2.0, SQLite, Caffeine |
| 2026-11-22 | feat(database): add initial SQLite schema with Flyway migration | V1__initial_schema.sql | Complete database schema for all 8 phases |
| 2026-11-22 | chore(frontend): initialize React + TypeScript + Vite project | package.json, vite.config.ts, tsconfig.json | React 18, TypeScript 5.3, TanStack Query |
| 2026-11-22 | chore(docker): add multi-stage Dockerfile and Docker Compose setup | Dockerfile, docker-compose.yml | Multi-stage build with security best practices |
| 2026-11-22 | ci: add GitHub Actions workflow | .github/workflows/ci.yml | Automated build and test pipeline |
| 2026-11-22 | feat(domain): add core domain classes and utilities | 4 Java files | IdGenerator, Project entity, enums |
| 2026-11-22 | feat(backend): add project repository and health endpoints | 2 Java files | JPA repository and management endpoints |
| 2026-11-22 | feat(frontend): add initial React app with Phase 0 placeholder | 4 frontend files | Working frontend with Phase 0 status page |

---

## Key Decisions

### Decision Log Format
```
Date: YYYY-MM-DD
Decision: Brief title
Context: Why this decision was needed
Options: What alternatives were considered
Choice: What was chosen
Rationale: Why this choice
Consequences: Impact on the project
```

### 2026-11-22: Modular Monolith Architecture
**Decision**: Use modular monolith instead of microservices  
**Context**: Need to balance simplicity with future extensibility  
**Choice**: Single Spring Boot process with logical module boundaries  
**Rationale**: Simpler local deployment, easier debugging, faster iteration  
**Consequences**: Must maintain clear module boundaries to enable future extraction

### 2026-11-22: SQLite as Primary Storage
**Decision**: Use SQLite for persistent storage instead of PostgreSQL/MySQL  
**Context**: Local-first product, no remote database required  
**Choice**: SQLite with WAL mode  
**Rationale**: Zero configuration, single file, perfect for local dev tools  
**Consequences**: Limited write concurrency, not suitable for distributed deployment

### 2026-11-22: JSON Record Storage
**Decision**: Store user-defined records as JSON instead of dynamic tables  
**Context**: Schema can change frequently, don't want DDL migrations per resource  
**Choice**: Store record bodies as JSON text in mock_records table  
**Rationale**: Flexible schema evolution, simpler implementation  
**Consequences**: Complex queries require in-memory filtering or JSON extension functions

---

## Milestones

| Date | Milestone | Phase | Status |
|------|-----------|-------|--------|
| 2026-11-22 | Project kickoff and documentation | Phase 0 | ✅ Complete |
| 2026-12-06 | Phase 0 complete: Technical spike validated | Phase 0 | ⏳ In Progress |
| TBD | Phase 1 complete: Core engine working | Phase 1 | ⚪ Planned |
| TBD | Phase 2 complete: Dashboard UI | Phase 2 | ⚪ Planned |
| TBD | v1.0 Release | Phase 8 | ⚪ Planned |

---

## Weekly Updates

### Week of November 22, 2026
**Phase**: 0 - Foundation  
**Progress**:
- ✅ Created comprehensive documentation structure
- ✅ Defined 8-phase development roadmap
- ✅ Established project conventions and repository structure
- ✅ Created brand identity and logo
- 🟡 In Progress: Spring Boot skeleton

**Blockers**: None  
**Next Week**: Complete technical spike, validate Docker volume persistence

---

## Statistics

### Phase 0
- **Commits**: 9
- **Files Created**: 40+
- **Documentation Pages**: 8
- **Lines of Code**: ~2,500+ (backend + frontend + config)

---

## Notes

- This file is updated after each significant commit or at least weekly
- Keep entries concise but informative
- Link to related issues/PRs when applicable
- Use emoji indicators: ✅ Complete, 🟡 In Progress, ⚪ Planned, ❌ Blocked

---

**Last Updated**: November 22, 2026  
**Current Phase**: Phase 0 - Foundation & Technical Spike  
**Total Commits**: 9
**Status**: ✅ All code pushed to GitHub
