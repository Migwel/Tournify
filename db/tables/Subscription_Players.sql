CREATE TABLE public.subscription_players (
    subscription_id uuid NOT NULL,
    player character varying(255),
    players_order integer NOT NULL
);