# Contributing to MockForge

Thank you for considering contributing to MockForge! This document provides guidelines and instructions for contributing.

## 🎯 Project Vision

MockForge is a local-first API mocking platform. Before contributing, please read:
- [README.md](../README.md) - Project overview
- [docs/DEVELOPMENT_PHASES.md](DEVELOPMENT_PHASES.md) - Development roadmap
- Architecture specification (PDF) - Complete technical design

## 📋 How to Contribute

### 1. Check Current Phase
Review [DEVELOPMENT_PHASES.md](DEVELOPMENT_PHASES.md) to see which phase we're in and what's currently being worked on.

### 2. Find or Create an Issue
- Check existing issues for bugs or feature requests
- For new features, create an issue first to discuss before implementing
- Reference the phase and requirement from the spec if applicable

### 3. Fork and Branch
```bash
# Fork the repository on GitHub
# Clone your fork
git clone https://github.com/YOUR_USERNAME/Mockforge.git
cd Mockforge

# Add upstream remote
git remote add upstream https://github.com/SUBHAM-RAJ-01/Mockforge.git

# Create a feature branch
git checkout -b feature/your-feature-name
# or
git checkout -b fix/your-bugfix-name
```

### 4. Make Changes
- Follow existing code style and conventions
- Write tests for new functionality
- Update documentation as needed
- Keep commits focused and atomic

### 5. Test Your Changes
```bash
# Backend tests
cd backend
mvn test

# Frontend tests
cd frontend
npm test

# Integration tests (if available)
./scripts/run-integration-tests.sh
```

### 6. Commit Guidelines
Follow conventional commit format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, no logic change)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Build process, dependencies, tooling

**Examples:**
```bash
git commit -m "feat(runtime): add dynamic route matcher for CRUD endpoints"

git commit -m "fix(storage): prevent race condition in hybrid write-through

- Add optimistic locking with version field
- Return 409 on stale update
- Add concurrent update test

Closes #42"

git commit -m "docs(api): document query parameter conventions"
```

### 7. Push and Create PR
```bash
# Push to your fork
git push origin feature/your-feature-name

# Create Pull Request on GitHub
# Reference the issue number in PR description
```

## 🏗️ Development Setup

### Prerequisites
- **Java**: JDK 21 or later
- **Maven**: 3.9+
- **Node.js**: 20+
- **Docker**: Latest stable
- **Git**: Latest stable

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

### Docker Build
```bash
docker build -t mockforge:local .
docker compose up
```

## 📏 Code Style

### Java
- Follow standard Java conventions
- Use Spring Boot best practices
- Keep methods focused and under 50 lines when possible
- Document public interfaces with Javadoc
- Use meaningful variable names

```java
// ✅ Good
public Optional<RuntimeSnapshot> getActiveSnapshot(String projectId) {
    return snapshotCache.get(projectId);
}

// ❌ Avoid
public Optional<RuntimeSnapshot> get(String id) { // Unclear what we're getting
    return c.get(id); // Unclear variable name
}
```

### TypeScript/React
- Use functional components with hooks
- Type all props and state
- Keep components under 200 lines
- Extract reusable logic into custom hooks
- Use meaningful component and variable names

```typescript
// ✅ Good
interface UserListProps {
  projectId: string;
  onUserSelect: (userId: string) => void;
}

export function UserList({ projectId, onUserSelect }: UserListProps) {
  // ...
}

// ❌ Avoid
export function List(props: any) { // Any type, unclear name
  // ...
}
```

### SQL/Database
- Use prepared statements always
- Keep transactions short
- Add indexes for frequently queried columns
- Version all migrations

## 🧪 Testing Guidelines

### Test Structure
```
describe/test naming: "should <behavior> when <condition>"
```

### Coverage Expectations
- **Unit tests**: Cover business logic, edge cases
- **Integration tests**: Cover API contracts, storage layer
- **Component tests**: Cover UI interactions
- **E2E tests** (Phase 7+): Cover critical user journeys

### Writing Good Tests
```java
@Test
void shouldReturnConflictWhenUpdatingStaleVersion() {
    // Arrange
    var record = createTestRecord();
    var staleVersion = record.getVersion();
    
    // Act - simulate concurrent update
    dataStore.update(record.getId(), changesA, staleVersion);
    var result = dataStore.update(record.getId(), changesB, staleVersion);
    
    // Assert
    assertThat(result).isConflict();
}
```

## 📖 Documentation

### When to Document
- New features or APIs
- Complex algorithms or logic
- Configuration options
- Breaking changes
- Migration guides

### Where to Document
- **Code comments**: Why, not what (code shows what)
- **README.md**: High-level overview and quickstart
- **docs/**: Detailed guides and architecture
- **Inline JSDoc/Javadoc**: Public APIs

## 🐛 Reporting Bugs

### Bug Report Template
```markdown
**Description**
Clear description of the bug

**Steps to Reproduce**
1. Step one
2. Step two
3. See error

**Expected Behavior**
What should happen

**Actual Behavior**
What actually happens

**Environment**
- OS: [e.g., macOS 14.0, Windows 11, Ubuntu 22.04]
- Docker version: [e.g., 24.0.6]
- MockForge version: [e.g., 0.1.0]

**Additional Context**
Screenshots, logs, etc.
```

## ✨ Feature Requests

### Feature Request Template
```markdown
**Problem**
What problem does this solve?

**Proposed Solution**
How should it work?

**Alternatives Considered**
What other approaches did you consider?

**Additional Context**
Any other relevant information
```

## 🔒 Security

If you discover a security vulnerability:
1. **DO NOT** create a public issue
2. Email security details to: [TBD]
3. Include steps to reproduce
4. Allow time for a fix before public disclosure

## 📜 License

By contributing, you agree that your contributions will be licensed under the MIT License.

## ❓ Questions?

- Check existing [issues](https://github.com/SUBHAM-RAJ-01/Mockforge/issues)
- Check [documentation](docs/)
- Create a discussion or issue

## 🎉 Recognition

Contributors will be recognized in:
- `CONTRIBUTORS.md` (TBD)
- Release notes for significant contributions
- Project README

---

**Thank you for contributing to MockForge!** 🚀

---

Last Updated: November 22, 2026
