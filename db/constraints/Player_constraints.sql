ALTER TABLE public.player OWNER TO user_tournify;

ALTER TABLE ONLY public.player
    ADD CONSTRAINT player_pkey PRIMARY KEY (id);