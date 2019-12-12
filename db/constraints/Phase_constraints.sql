ALTER TABLE public.phase OWNER TO user_tournify;

ALTER TABLE ONLY public.phase
    ADD CONSTRAINT phase_pkey PRIMARY KEY (id);