-- MockForge Initial Database Schema
-- Version: 1.0
-- Phase: 0 - Foundation

-- Projects table
CREATE TABLE projects (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    slug TEXT NOT NULL UNIQUE,
    base_path TEXT NOT NULL,
    storage_mode TEXT NOT NULL CHECK(storage_mode IN ('EPHEMERAL', 'PERSISTENT', 'HYBRID')),
    status TEXT NOT NULL DEFAULT 'STOPPED' CHECK(status IN ('STOPPED', 'RUNNING', 'ERROR')),
    config_version BIGINT NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now'))
);

-- Resources table
CREATE TABLE resources (
    id TEXT PRIMARY KEY,
    project_id TEXT NOT NULL,
    name TEXT NOT NULL,
    slug TEXT NOT NULL,
    schema_version BIGINT NOT NULL DEFAULT 1,
    timestamps BOOLEAN NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    UNIQUE(project_id, slug)
);

-- Resource fields table
CREATE TABLE resource_fields (
    id TEXT PRIMARY KEY,
    resource_id TEXT NOT NULL,
    name TEXT NOT NULL,
    field_type TEXT NOT NULL,
    required BOOLEAN NOT NULL DEFAULT 0,
    unique_flag BOOLEAN NOT NULL DEFAULT 0,
    default_json TEXT,
    config_json TEXT,
    position INTEGER NOT NULL,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE,
    UNIQUE(resource_id, name)
);

-- Relationships table
CREATE TABLE relationships (
    id TEXT PRIMARY KEY,
    source_resource_id TEXT NOT NULL,
    target_resource_id TEXT NOT NULL,
    relation_type TEXT NOT NULL CHECK(relation_type IN ('ONE_TO_ONE', 'ONE_TO_MANY', 'MANY_TO_ONE', 'MANY_TO_MANY')),
    source_field TEXT,
    target_field TEXT,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (source_resource_id) REFERENCES resources(id) ON DELETE CASCADE,
    FOREIGN KEY (target_resource_id) REFERENCES resources(id) ON DELETE CASCADE
);

-- Mock records table (stores JSON data)
CREATE TABLE mock_records (
    id TEXT NOT NULL,
    resource_id TEXT NOT NULL,
    data_json TEXT NOT NULL,
    version BIGINT NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    PRIMARY KEY (resource_id, id),
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
);

-- Endpoints table
CREATE TABLE endpoints (
    id TEXT PRIMARY KEY,
    project_id TEXT NOT NULL,
    method TEXT NOT NULL,
    path TEXT NOT NULL,
    handler_type TEXT NOT NULL,
    config_json TEXT,
    enabled BOOLEAN NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    UNIQUE(project_id, method, path)
);

-- Scenarios table
CREATE TABLE scenarios (
    id TEXT PRIMARY KEY,
    project_id TEXT NOT NULL,
    name TEXT NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT 0,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    UNIQUE(project_id, name)
);

-- Simulation rules table
CREATE TABLE simulation_rules (
    id TEXT PRIMARY KEY,
    endpoint_id TEXT,
    scenario_id TEXT,
    priority INTEGER NOT NULL DEFAULT 0,
    rule_json TEXT NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (endpoint_id) REFERENCES endpoints(id) ON DELETE CASCADE,
    FOREIGN KEY (scenario_id) REFERENCES scenarios(id) ON DELETE CASCADE
);

-- Request logs table (bounded retention)
CREATE TABLE request_logs (
    id TEXT PRIMARY KEY,
    project_id TEXT NOT NULL,
    request_id TEXT NOT NULL,
    method TEXT NOT NULL,
    path TEXT NOT NULL,
    status INTEGER NOT NULL,
    duration_ms INTEGER NOT NULL,
    summary_json TEXT,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_resources_project ON resources(project_id);
CREATE INDEX idx_resource_fields_resource ON resource_fields(resource_id);
CREATE INDEX idx_mock_records_resource ON mock_records(resource_id);
CREATE INDEX idx_endpoints_project ON endpoints(project_id);
CREATE INDEX idx_scenarios_project ON scenarios(project_id);
CREATE INDEX idx_request_logs_project_created ON request_logs(project_id, created_at DESC);

-- Insert default data
INSERT INTO projects (id, name, slug, base_path, storage_mode, status)
VALUES ('proj_default', 'Default Project', 'default', '/api', 'HYBRID', 'STOPPED');
