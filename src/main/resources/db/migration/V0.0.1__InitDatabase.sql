create table sources
(
    id            BIGSERIAL PRIMARY KEY,
    type          text not null,
    userId        text not null,
    credentials   jsonb,
    configuration jsonb
);

create table targets
(
    id            BIGSERIAL PRIMARY KEY,
    type          text not null,
    userId        text not null,
    configuration jsonb,
    credentials   jsonb
);

create table connections
(
    id                 BIGSERIAL PRIMARY KEY,
    lastSynchronizedAt timestamp(6),
    source_id          bigint references sources (id),
    target_id          bigint references targets (id),
    targetUploadPath   text,
    userId             text not null,
    status text,
    lastDocumentDownloadedDate timestamp(6)
);

create table synchronizations
(
    id            BIGSERIAL PRIMARY KEY,
    connection_id BIGINT references connections (id),
    status        TEXT,
    error         TEXT,
    endedAt       timestamp(6),
    startedAt     timestamp(6)
);