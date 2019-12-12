ALTER TABLE public.notification OWNER TO user_tournify;

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT fkfo8e7i76bangkc0i1vq3x25p9 FOREIGN KEY (subscription_id) REFERENCES public.subscription(id);
