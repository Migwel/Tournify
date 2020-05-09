ALTER TABLE public.set OWNER TO user_tournify;

ALTER TABLE ONLY public.set
    ADD CONSTRAINT set_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.set
    ADD CONSTRAINT fkp7433nons6n9n9kg2e9ciggqp FOREIGN KEY (phase_id) REFERENCES public.phase(id);