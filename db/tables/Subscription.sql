CREATE TABLE public.subscription (
    id uuid NOT NULL,
    active boolean NOT NULL,
    callback_url character varying(255) not null,
    username character varying,
    password character varying,
    tournament_id bigint
);