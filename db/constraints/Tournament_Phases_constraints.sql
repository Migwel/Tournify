ALTER TABLE public.tournament_phases OWNER TO user_tournify;

ALTER TABLE ONLY public.tournament_phases
    ADD CONSTRAINT uk_kl5aykpqy9ax0ggrxxyd88pdy UNIQUE (phases_id);

ALTER TABLE ONLY public.tournament_phases
    ADD CONSTRAINT fkp7433nons6n9n9kg2e9ciggqp FOREIGN KEY (phases_id) REFERENCES public.phase(id);

ALTER TABLE ONLY public.tournament_phases
    ADD CONSTRAINT fkri3hm91qqwvpp2uo4tglv43vv FOREIGN KEY (tournament_id) REFERENCES public.tournament(id);
