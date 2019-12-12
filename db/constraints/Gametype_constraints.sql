ALTER TABLE public.gametype OWNER TO user_tournify;

ALTER TABLE ONLY public.gametype
    ADD CONSTRAINT gametype_pkey PRIMARY KEY (id);