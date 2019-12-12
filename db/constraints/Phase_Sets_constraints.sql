ALTER TABLE ONLY public.phase_sets
    ADD CONSTRAINT uk_slf4t3ulunpjndjmglmprknnu UNIQUE (sets_id);

ALTER TABLE ONLY public.phase_sets
    ADD CONSTRAINT fkhkt415pqppcx0y4mv09tvstd7 FOREIGN KEY (sets_id) REFERENCES public.set(id);

ALTER TABLE ONLY public.phase_sets
    ADD CONSTRAINT fkkdxheq1g3cbkutiybauugqrq4 FOREIGN KEY (phase_id) REFERENCES public.phase(id);
