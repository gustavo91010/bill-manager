BEGIN;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigserial NOT NULL,
    accessToken character varying(100) NOT NULL,
    active boolean NOT NULL,
    PRIMARY KEY (id)
);

END;
