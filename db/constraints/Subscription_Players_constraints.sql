ALTER TABLE ONLY public.subscription_players
    ADD CONSTRAINT subscription_players_pkey PRIMARY KEY (subscription_id, players_order);

ALTER TABLE ONLY public.subscription_players
    ADD CONSTRAINT fk6gs4l1p4rjnoibt2cfblw5g9e FOREIGN KEY (subscription_id) REFERENCES public.subscription(id);

