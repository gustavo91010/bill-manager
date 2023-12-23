BEGIN;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigserial NOT NULL,
    name character varying(100) NOT NULL,
    email character varying(200) NOT NULL,
    active boolean NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NULL,
    PRIMARY KEY (id)
);

END;