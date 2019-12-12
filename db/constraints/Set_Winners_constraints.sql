ALTER TABLE public.set_winners OWNER TO user_tournify;

ALTER TABLE ONLY public.set_winners
    ADD CONSTRAINT fkhum1sm46mnwmfkp0j4su7weyp FOREIGN KEY (set_id) REFERENCES public.set(id);

ALTER TABLE ONLY public.set_winners
    ADD CONSTRAINT fkrgquolu862h87k0apu9sf0va9 FOREIGN KEY (winners_id) REFERENCES public.player(id);