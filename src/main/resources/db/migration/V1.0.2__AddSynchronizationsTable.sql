create table synchronizations
(
    id            BIGSERIAL PRIMARY KEY,
    connection_id BIGINT references connections (id),
    status        TEXT,
    error         TEXT,
    endedAt       timestamp(6),
    startedAt     timestamp(6)
);