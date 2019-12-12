CREATE TABLE public.tournamenttracking (
    id bigint NOT NULL,
    done boolean NOT NULL,
    next_date timestamp without time zone,
    no_update_retries integer NOT NULL,
    start_date timestamp without time zone,
    tournament_id bigint
);