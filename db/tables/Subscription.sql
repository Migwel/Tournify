CREATE TABLE public.subscription (
    id uuid NOT NULL,
    active boolean NOT NULL,
    callback_url character varying(255),
    tournament_id bigint
);