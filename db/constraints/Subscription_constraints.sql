ALTER TABLE public.subscription OWNER TO user_tournify;

ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT subscription_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT ukfkvl0oyku35cvq3guhb581qbp UNIQUE (tournament_id, callback_url);

ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT fk1gl93f90rrgfaxym622n2bs8a FOREIGN KEY (tournament_id) REFERENCES public.tournament(id);
