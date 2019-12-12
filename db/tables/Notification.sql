CREATE TABLE public.notification (
    id bigint NOT NULL,
    content text,
    done boolean NOT NULL,
    next_date timestamp without time zone,
    no_update_retries integer NOT NULL,
    start_date timestamp without time zone,
    subscription_id uuid
);