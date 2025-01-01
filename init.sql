CREATE SCHEMA IF NOT EXISTS mtcg;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'damage_type') THEN
        CREATE TYPE mtcg.damage_type AS ENUM ('fire', 'water', 'normal');
    END IF;
END
$$;
ALTER TYPE mtcg.damage_type OWNER TO mtcgdb;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'card_type') THEN
        CREATE TYPE mtcg.card_type AS ENUM ('monster', 'spell');
    END IF;
END
$$;
ALTER TYPE mtcg.card_type OWNER TO mtcgdb;

CREATE TABLE IF NOT EXISTS mtcg."user"
(
    id BIGSERIAL PRIMARY KEY,
    token uuid NOT NULL,
    username character varying(32) COLLATE pg_catalog."default" NOT NULL,
    password character varying(32) COLLATE pg_catalog."default" NOT NULL,
    bio character varying(64) COLLATE pg_catalog."default",
    image character varying(8) COLLATE pg_catalog."default",
    coins integer,
    elo integer,
    wins integer,
    losses integer,
    UNIQUE (token),
    UNIQUE (username)
);

ALTER TABLE IF EXISTS mtcg."user" OWNER to mtcgdb;

CREATE TABLE IF NOT EXISTS mtcg.session
(
    id BIGSERIAL PRIMARY KEY,
    token character varying(64) NOT NULL,
    fk_user_id bigint NOT NULL,
    UNIQUE (token),
    CONSTRAINT fk_user_id FOREIGN KEY (fk_user_id)
    REFERENCES mtcg."user" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS mtcg.session OWNER to mtcgdb;

CREATE TABLE IF NOT EXISTS mtcg."package"
(
    id BIGSERIAL PRIMARY KEY,
    token uuid NOT NULL,
    price integer NOT NULL,
    UNIQUE (token)
);

ALTER TABLE IF EXISTS mtcg."package" OWNER to mtcgdb;

CREATE TABLE IF NOT EXISTS mtcg.card
(
    id BIGSERIAL PRIMARY KEY,
    token uuid NOT NULL,
    name character varying(32) COLLATE pg_catalog."default" NOT NULL,
    damage double precision NOT NULL,
    damage_type mtcg.damage_type NOT NULL,
    in_deck boolean NOT NULL DEFAULT false,
    fk_user_id bigint,
    fk_package_id bigint,
    UNIQUE (token),
    CONSTRAINT fk_package_id FOREIGN KEY (fk_package_id)
    REFERENCES mtcg."package" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk_user_id FOREIGN KEY (fk_user_id)
    REFERENCES mtcg."user" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS mtcg.card OWNER to mtcgdb;

CREATE TABLE IF NOT EXISTS mtcg.monster_card
(
    defence double precision
)
INHERITS (mtcg.card);

ALTER TABLE IF EXISTS mtcg.monster_card OWNER to mtcgdb;

CREATE TABLE IF NOT EXISTS mtcg.spell_card
(
    critical_hit_chance double precision
)
INHERITS (mtcg.card);

ALTER TABLE IF EXISTS mtcg.spell_card OWNER to mtcgdb;

CREATE TABLE IF NOT EXISTS mtcg.trade
(
    id BIGSERIAL PRIMARY KEY,
    token uuid NOT NULL,
    "minimumDamage" double precision NOT NULL,
    type mtcg.card_type NOT NULL,
    fk_card_id bigint NOT NULL,
    fk_user_id bigint NOT NULL,
    UNIQUE (token),
    CONSTRAINT fk_card_id FOREIGN KEY (fk_card_id)
    REFERENCES mtcg.card (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk_user_id FOREIGN KEY (fk_user_id)
    REFERENCES mtcg."user" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS mtcg.trade OWNER to mtcgdb;