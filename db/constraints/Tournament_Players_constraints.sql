ALTER TABLE public.tournament_players OWNER TO user_tournify;

ALTER TABLE ONLY public.tournament_players
    ADD CONSTRAINT fkl5juw41skifnx7jjpkw5iyhk5 FOREIGN KEY (players_id) REFERENCES public.player(id);

ALTER TABLE ONLY public.tournament_players
    ADD CONSTRAINT fko70tr2qy8aysskc14vi4kmmk7 FOREIGN KEY (tournament_id) REFERENCES public.tournament(id);
