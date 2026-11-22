import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter } from 'react-router-dom'
import './App.css'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
})

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <div className="app">
          <header className="app-header">
            <div className="logo-container">
              <img src="/logo.svg" alt="MockForge" className="logo" />
              <h1>MockForge</h1>
            </div>
            <p className="tagline">Local-first API Mocking Platform</p>
          </header>
          
          <main className="app-main">
            <div className="welcome-card">
              <h2>🚧 Under Construction</h2>
              <p>MockForge is currently in <strong>Phase 0: Foundation</strong></p>
              <p className="phase-info">
                We're building the core architecture and technical foundation.
                The dashboard UI will be available in Phase 2.
              </p>
              
              <div className="status-section">
                <h3>Current Status</h3>
                <ul className="status-list">
                  <li>✅ Project structure and documentation</li>
                  <li>✅ Backend skeleton (Spring Boot + SQLite)</li>
                  <li>✅ Database schema (Flyway)</li>
                  <li>✅ Docker setup (multi-stage build)</li>
                  <li>✅ CI/CD pipeline (GitHub Actions)</li>
                  <li>🟡 In Progress: Technical spike validation</li>
                </ul>
              </div>

              <div className="links-section">
                <h3>Resources</h3>
                <ul className="links-list">
                  <li>
                    <a href="/_mockforge/health" target="_blank" rel="noopener noreferrer">
                      Health Check
                    </a>
                  </li>
                  <li>
                    <a href="/_mockforge/diagnostics" target="_blank" rel="noopener noreferrer">
                      Diagnostics
                    </a>
                  </li>
                  <li>
                    <a href="https://github.com/SUBHAM-RAJ-01/Mockforge" target="_blank" rel="noopener noreferrer">
                      GitHub Repository
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </main>

          <footer className="app-footer">
            <p>MockForge v0.1.0-SNAPSHOT | Phase 0 - Foundation</p>
          </footer>
        </div>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

export default App
