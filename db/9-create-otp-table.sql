SET SEARCH_PATH TO premiere;

CREATE TABLE IF NOT EXISTS "otp"
(
    "id"         SERIAL       NOT NULL,
    "otp"        VARCHAR(255) NOT NULL,
    "email"      VARCHAR(255) NOT NULL,
    "request_id" INT          NULL
) INHERITS ("premiere_abstract_table")
  WITH (OIDS= FALSE);