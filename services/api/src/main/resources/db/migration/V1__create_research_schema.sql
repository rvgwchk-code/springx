CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE tenants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    slug TEXT NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE app_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    issuer TEXT NOT NULL,
    subject TEXT NOT NULL,
    email TEXT,
    display_name TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (tenant_id, issuer, subject),
    UNIQUE (id, tenant_id)
);

CREATE INDEX app_users_tenant_email_idx ON app_users (tenant_id, email);

CREATE TABLE source_connections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    owner_user_id UUID REFERENCES app_users(id) ON DELETE SET NULL,
    provider TEXT NOT NULL CHECK (provider IN ('upload', 'url', 'local', 'sharepoint', 'onedrive', 'google_drive')),
    ownership TEXT NOT NULL CHECK (ownership IN ('delegated', 'shared')),
    status TEXT NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'connected', 'syncing', 'warning', 'disconnected')),
    external_id TEXT,
    display_name TEXT NOT NULL,
    base_uri TEXT,
    permissions JSONB NOT NULL DEFAULT '[]'::jsonb,
    last_sync_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (id, tenant_id),
    UNIQUE (tenant_id, provider, external_id)
);

CREATE INDEX source_connections_tenant_status_idx ON source_connections (tenant_id, status);
CREATE INDEX source_connections_owner_idx ON source_connections (owner_user_id);

CREATE TABLE documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    source_id UUID NOT NULL,
    external_id TEXT NOT NULL,
    title TEXT NOT NULL,
    mime_type TEXT,
    source_uri TEXT NOT NULL,
    content_hash TEXT,
    extracted_text TEXT,
    metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at TIMESTAMPTZ,
    UNIQUE (id, tenant_id),
    UNIQUE (source_id, external_id),
    FOREIGN KEY (source_id, tenant_id) REFERENCES source_connections(id, tenant_id) ON DELETE CASCADE
);

CREATE INDEX documents_tenant_source_idx ON documents (tenant_id, source_id);
CREATE INDEX documents_tenant_updated_idx ON documents (tenant_id, updated_at DESC);
CREATE INDEX documents_content_hash_idx ON documents (tenant_id, content_hash);

CREATE TABLE document_chunks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    document_id UUID NOT NULL,
    ordinal INTEGER NOT NULL CHECK (ordinal >= 0),
    content TEXT NOT NULL,
    location TEXT,
    start_offset INTEGER NOT NULL DEFAULT 0 CHECK (start_offset >= 0),
    end_offset INTEGER NOT NULL DEFAULT 0 CHECK (end_offset >= start_offset),
    search_vector TSVECTOR GENERATED ALWAYS AS (to_tsvector('english'::regconfig, content)) STORED,
    embedding VECTOR(1536),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (document_id, ordinal),
    FOREIGN KEY (document_id, tenant_id) REFERENCES documents(id, tenant_id) ON DELETE CASCADE
);

CREATE INDEX document_chunks_tenant_document_idx ON document_chunks (tenant_id, document_id);
CREATE INDEX document_chunks_search_vector_idx ON document_chunks USING GIN (search_vector);
CREATE INDEX document_chunks_embedding_idx ON document_chunks USING HNSW (embedding vector_cosine_ops);

CREATE TABLE document_acls (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    document_id UUID NOT NULL,
    principal_type TEXT NOT NULL CHECK (principal_type IN ('user', 'group', 'tenant')),
    principal_id TEXT NOT NULL,
    permission TEXT NOT NULL CHECK (permission IN ('read', 'write', 'admin')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (document_id, principal_type, principal_id),
    FOREIGN KEY (document_id, tenant_id) REFERENCES documents(id, tenant_id) ON DELETE CASCADE
);

CREATE INDEX document_acls_principal_idx ON document_acls (tenant_id, principal_type, principal_id);

CREATE TABLE source_sync_cursors (
    source_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    cursor JSONB NOT NULL DEFAULT '{}'::jsonb,
    last_success_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    FOREIGN KEY (source_id, tenant_id) REFERENCES source_connections(id, tenant_id) ON DELETE CASCADE
);

CREATE INDEX source_sync_cursors_tenant_idx ON source_sync_cursors (tenant_id);

CREATE TABLE ingestion_jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    source_id UUID NOT NULL,
    document_id UUID,
    job_type TEXT NOT NULL CHECK (job_type IN ('full_sync', 'incremental_sync', 'ingest', 'delete', 'permission_refresh')),
    status TEXT NOT NULL DEFAULT 'queued' CHECK (status IN ('queued', 'running', 'succeeded', 'failed', 'cancelled')),
    attempt_count INTEGER NOT NULL DEFAULT 0 CHECK (attempt_count >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    started_at TIMESTAMPTZ,
    finished_at TIMESTAMPTZ,
    error_message TEXT,
    FOREIGN KEY (source_id, tenant_id) REFERENCES source_connections(id, tenant_id) ON DELETE CASCADE,
    FOREIGN KEY (document_id, tenant_id) REFERENCES documents(id, tenant_id) ON DELETE CASCADE
);

CREATE INDEX ingestion_jobs_tenant_status_created_idx ON ingestion_jobs (tenant_id, status, created_at DESC);
CREATE INDEX ingestion_jobs_source_created_idx ON ingestion_jobs (source_id, created_at DESC);