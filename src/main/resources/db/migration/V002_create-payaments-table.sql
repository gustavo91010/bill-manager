BEGIN;

CREATE TABLE IF NOT EXISTS public.payment
(
    status character varying(20) NOT NULL,
    id bigserial NOT NULL,
    description character varying(100),
    value numeric(10, 2) NOT NULL,
    due_date timestamp with time zone NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NULL,
	user_id bigserial REFERENCES users(id),
    PRIMARY KEY (id)
);


    
END;
