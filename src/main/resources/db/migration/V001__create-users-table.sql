BEGIN;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigserial PRIMARY KEY,
    access_token character varying(100) NOT NULL,
    active boolean NOT NULL DEFAULT true
);

COMMIT;
