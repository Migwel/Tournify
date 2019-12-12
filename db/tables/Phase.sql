CREATE TABLE public.phase (
    id bigint NOT NULL,
    done boolean NOT NULL,
    external_id character varying(255),
    name character varying(255)
);