ALTER TABLE public.tournamenttracking OWNER TO user_tournify;

ALTER TABLE ONLY public.tournamenttracking
    ADD CONSTRAINT tournament_tracking_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.tournamenttracking
    ADD CONSTRAINT fklaefrb0pjbecr3nqqs0xjglv1 FOREIGN KEY (tournament_id) REFERENCES public.tournament(id);
