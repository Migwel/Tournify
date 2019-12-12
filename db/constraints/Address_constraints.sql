ALTER TABLE public.address OWNER TO user_tournify;

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);

