ALTER TABLE public.tournament OWNER TO user_tournify;

ALTER TABLE ONLY public.tournament
    ADD CONSTRAINT tournament_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.tournament
    ADD CONSTRAINT uk_j52vrytlqbmf2n81mjs9w98fe UNIQUE (url);

ALTER TABLE ONLY public.tournament
    ADD CONSTRAINT fkaevtmf9o8l2g4teqviuxw7yny FOREIGN KEY (game_type_id) REFERENCES public.game_type(id);

ALTER TABLE ONLY public.tournament
    ADD CONSTRAINT fkr1ywg9h1n3b4okxes0yjeaw66 FOREIGN KEY (address_id) REFERENCES public.address(id);
