CREATE TABLE error_message (
    id BIGSERIAL PRIMARY KEY,
    topic VARCHAR(40) NOT NULL,
    access_token VARCHAR(100) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    metadata JSONB
);

