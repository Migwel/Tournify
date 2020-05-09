ALTER TABLE public.phase OWNER TO user_tournify;

ALTER TABLE ONLY public.phase
    ADD CONSTRAINT phase_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.phase
    ADD CONSTRAINT fkri3hm91qqwvpp2uo4tglv43vv FOREIGN KEY (tournament_id) REFERENCES public.tournament(id);
