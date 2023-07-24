BEGIN;

CREATE TABLE IF NOT EXISTS public.payament
(
    status character varying(20) NOT NULL,
    id bigserial NOT NULL,
    description character varying(100),
    value numeric(10, 2) NOT NULL,
    due_date timestamp with time zone NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.payament
    ADD FOREIGN KEY (id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
    
END;