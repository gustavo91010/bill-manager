CREATE TABLE error_message (
    id BIGSERIAL PRIMARY KEY,
    topic VARCHAR(40) NOT NULL,
    access_token VARCHAR(100) NOT NULL,
    metadata JSONB
);

