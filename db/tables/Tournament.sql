CREATE TABLE public.tournament (
    id bigint NOT NULL,
    date timestamp without time zone,
    done boolean NOT NULL,
    external_id character varying(255),
    name character varying(255),
    url character varying(255),
    address_id bigint,
    game_type_id bigint
);