CREATE TABLE IF NOT EXISTS users
(
    user_id bigint NOT NULL,
    username text,
    first_name text NOT NULL,
    last_name text,
    lang_code text NOT NULL,
    is_bot boolean NOT NULL,
    timestamp_registration timestamp(6) with time zone NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS url_history
(
    url_id bigserial NOT NULL,
    url text,
    type character varying(10),
    is_sent boolean,
    timestamp_request timestamp(6) with time zone,
    user_id bigint,
    CONSTRAINT url_history_pkey PRIMARY KEY (url_id),
    CONSTRAINT url_history_fk_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS users_bot_lang
(
    user_id bigint NOT NULL,
    lang_code character varying(5),
    timestamp_set timestamp(6) with time zone,
    CONSTRAINT users_bot_lang_pkey PRIMARY KEY (user_id),
    CONSTRAINT user_bot_lang_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);