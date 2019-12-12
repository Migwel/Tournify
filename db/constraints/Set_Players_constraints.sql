ALTER TABLE public.set_players OWNER TO user_tournify;

ALTER TABLE ONLY public.set_players
    ADD CONSTRAINT fkl5juw41skifnx7jjpkw5iyhk8 FOREIGN KEY (players_id) REFERENCES public.player(id);

ALTER TABLE ONLY public.set_players
    ADD CONSTRAINT fko70tr2qy8aysskc14vi4kmmk8 FOREIGN KEY (set_id) REFERENCES public.set(id);
