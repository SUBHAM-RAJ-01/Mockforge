# MockForge

**Local-first API Mocking & Backend Simulation Platform**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.x-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-blue.svg)](https://www.typescriptlang.org/)

## 🎯 Product Vision

Design resources. Generate APIs. Simulate backend behavior locally — without writing or deploying a backend.

MockForge is a Dockerized developer tool for defining resources, generating REST APIs, creating realistic mock data, simulating backend behavior and inspecting frontend requests.

## ✨ Key Features

- **Local-first**: Works on your machine without cloud dependencies
- **Schema-driven**: Define resources with fields, constraints, and relationships
- **Dynamic CRUD**: Automatic REST endpoints from resource definitions
- **Mock Data Generation**: Seeded, reproducible fake data
- **Simulation Engine**: Delays, failures, and scenarios for testing edge cases
- **Request Inspector**: View, replay, and debug API calls
- **Three Storage Modes**: Ephemeral, Persistent (SQLite), or Hybrid
- **Docker Ready**: Single command to start

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/SUBHAM-RAJ-01/Mockforge.git
cd Mockforge

# Start with Docker Compose
docker compose up -d

# Open dashboard
open http://localhost:8080

# Health check
curl http://localhost:8080/_mockforge/health
```

## 📋 Current Development Phase

**Phase 0: Foundation & Spike** (In Progress)

See [docs/DEVELOPMENT_PHASES.md](docs/DEVELOPMENT_PHASES.md) for the complete roadmap.

## 📚 Documentation

- [Architecture Overview](docs/architecture.md)
- [Development Phases](docs/DEVELOPMENT_PHASES.md)
- [API Contracts](docs/api-contracts.md)
- [Contributing Guide](docs/CONTRIBUTING.md)
- [Commit History](docs/COMMIT_HISTORY.md)

## 🏗️ Architecture

MockForge is built as a **modular monolith** with two logical planes:

- **Management Plane**: Project configuration, resource schemas, data management
- **Data Plane**: Dynamic request routing, CRUD handlers, simulation engine

**Tech Stack:**
- Backend: Java 21 + Spring Boot 3.x
- Frontend: React 18 + TypeScript 5.x
- Storage: SQLite + In-Memory + Caffeine Cache
- Deployment: Docker + Docker Compose

## 🎨 Core Principles

1. **Local-first**: Core workflow works offline on a developer machine
2. **Fast feedback**: Resource definition → callable API in minimal steps
3. **Predictable**: Deterministic generation and reproducible scenarios
4. **Safe by default**: Loopback binding, bounded resources, no arbitrary code execution
5. **Progressive complexity**: Simple CRUD is easy; relationships and simulations are optional

## 📦 Project Structure

```
mockforge/
├── backend/           # Java Spring Boot application
├── frontend/          # React TypeScript SPA
├── docs/             # Documentation and ADRs
├── examples/         # Sample projects
├── scripts/          # Build and development scripts
└── docker-compose.yml
```

## 🛠️ Development

```bash
# Backend (requires Java 21 + Maven)
cd backend
mvn spring-boot:run

# Frontend (requires Node.js 20+)
cd frontend
npm install
npm run dev

# Build Docker image
docker build -t mockforge:local .
```

## 🧪 Testing

```bash
# Backend tests
cd backend
mvn test

# Frontend tests
cd frontend
npm test

# Integration tests
./scripts/run-integration-tests.sh
```

## 📝 License

MIT License - see [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](docs/CONTRIBUTING.md) for details on our development process.

## 📧 Contact

- GitHub: [@SUBHAM-RAJ-01](https://github.com/SUBHAM-RAJ-01)
- Repository: [Mockforge](https://github.com/SUBHAM-RAJ-01/Mockforge)

---

**Status**: 🚧 Under Active Development

Last Updated: November 22, 2026
