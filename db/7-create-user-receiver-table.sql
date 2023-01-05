SET SEARCH_PATH TO premiere;

CREATE TABLE IF NOT EXISTS "user_receiver"
(
    "user_id"     INTEGER      NOT NULL,
    "receiver_id" INTEGER      NOT NULL,
    "nickname"    VARCHAR(255) NOT NULL,
    "full_name"   VARCHAR(255) NOT NULL,
    CONSTRAINT "user_receiver_pk" PRIMARY KEY ("user_id", "receiver_id"),
    CONSTRAINT "user_fk" FOREIGN KEY ("user_id") REFERENCES "user" ("id"),
    CONSTRAINT "receiver_fk" FOREIGN KEY ("receiver_id") REFERENCES "receiver" ("id")
) INHERITS ("premiere_abstract_table")
  WITH (OIDS= FALSE);

ALTER TABLE "receiver"
    DROP COLUMN IF EXISTS "user_id" CASCADE;
ALTER TABLE "receiver"
    DROP COLUMN IF EXISTS "nickname";
ALTER TABLE "receiver"
    DROP COLUMN IF EXISTS "full_name";

INSERT INTO "user_receiver" ("user_id", "receiver_id", "nickname", "full_name")
VALUES (1, 2, 'A', 'Nguyen Van A');
INSERT INTO "user_receiver" ("user_id", "receiver_id", "nickname", "full_name")
VALUES (1, 3, 'A', 'Nguyen Van A');
INSERT INTO "user_receiver" ("user_id", "receiver_id", "nickname", "full_name")
VALUES (2, 3, 'A', 'Nguyen Van A');
INSERT INTO "user_receiver" ("user_id", "receiver_id", "nickname", "full_name")
VALUES (3, 1, 'A', 'Nguyen Van A');
INSERT INTO "user_receiver" ("user_id", "receiver_id", "nickname", "full_name")
VALUES (3, 2, 'A', 'Nguyen Van A');