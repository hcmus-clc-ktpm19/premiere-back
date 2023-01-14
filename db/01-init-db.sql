CREATE SCHEMA premiere;
SET SEARCH_PATH TO premiere;

CREATE TYPE GENDER AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE LOAN_STATUS AS ENUM ('APPROVED', 'REJECTED', 'PENDING');
CREATE TYPE TRANSACTION_TYPE AS ENUM ('PURCHASE', 'REDEMPTION', 'LOAN');
CREATE TYPE TRANSACTION_STATUS AS ENUM ('CHECKING', 'COMPLETED', 'FAILED');
CREATE TYPE CARD_STATUS AS ENUM ('AVAILABLE', 'DISABLED');

CREATE TABLE "premiere_abstract_table"
(
    "version"    INT          NOT NULL DEFAULT 0,
    "created_at" TIMESTAMP(0) NOT NULL DEFAULT NOW(),
    "updated_at" TIMESTAMP(0) NOT NULL DEFAULT NOW()
);

CREATE TABLE "user"
(
    "id"         SERIAL       NOT NULL,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name"  VARCHAR(255) NOT NULL,
    "dob"        DATE,
    "gender"     GENDER       NOT NULL,
    "email"      VARCHAR(255) NOT NULL UNIQUE,
    "phone"      VARCHAR(10)  NOT NULL UNIQUE,
    "pan_number" VARCHAR(255) NOT NULL,
    "address"    VARCHAR(255) NOT NULL,
    "avatar"     VARCHAR(255),
    CONSTRAINT "user_pk" PRIMARY KEY ("id")
) INHERITS ("premiere_abstract_table") WITH (OIDS= FALSE);

CREATE TABLE "credit_card"
(
    "id"          SERIAL       NOT NULL,
    "user_id"     INTEGER      NOT NULL UNIQUE,
    "balance"     NUMERIC      NOT NULL,
    "open_day"    TIMESTAMP    NOT NULL,
    "card_number" VARCHAR(255) NOT NULL UNIQUE,
    "status"      CARD_STATUS  NOT NULL,
    CONSTRAINT "credit_card_pk" PRIMARY KEY ("id")
) INHERITS ("premiere_abstract_table") WITH (OIDS= FALSE);

CREATE TABLE "bank"
(
    "id"        SERIAL       NOT NULL,
    "bank_name" VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT "bank_pk" PRIMARY KEY ("id")
) INHERITS ("premiere_abstract_table") WITH (OIDS= FALSE);

CREATE TABLE "receiver"
(
    "id"          SERIAL       NOT NULL,
    "card_number" VARCHAR(255) NOT NULL UNIQUE,
    "nickname"    VARCHAR(255) NOT NULL,
    "full_name"   VARCHAR(255) NOT NULL,
    "user_id"     INTEGER      NOT NULL,
    "bank_id"     INTEGER      NOT NULL,
    CONSTRAINT "receivers_pk" PRIMARY KEY ("id")
) INHERITS ("premiere_abstract_table") WITH (OIDS= FALSE);

CREATE TABLE "loan_reminder"
(
    "id"                      SERIAL       NOT NULL,
    "loan_balance"            NUMERIC      NOT NULL,
    "sender_credit_card_id"   INTEGER      NOT NULL,
    "receiver_credit_card_id" INTEGER      NOT NULL,
    "status"                  LOAN_STATUS  NOT NULL,
    "time"                    TIMESTAMP    NOT NULL,
    "loan_remark"             VARCHAR(255) NOT NULL,
    "cancel_reason"           VARCHAR(255),
    CONSTRAINT "loan_reminder_pk" PRIMARY KEY ("id")
) INHERITS ("premiere_abstract_table") WITH (OIDS= FALSE);

CREATE TABLE "transaction"
(
    "id"                          SERIAL             NOT NULL,
    "amount"                      NUMERIC            NOT NULL,
    "type"                        TRANSACTION_TYPE   NOT NULL,
    "transaction_remark"          VARCHAR(255)       NOT NULL,
    "sender_balance"              NUMERIC            NOT NULL,
    "receiver_balance"            NUMERIC            NOT NULL,
    "sender_credit_card_number"   VARCHAR(255)       NOT NULL,
    "receiver_credit_card_number" VARCHAR(255)       NOT NULL,
    "sender_bank_id"              INTEGER            NOT NULL,
    "receiver_bank_id"            INTEGER            NOT NULL,
    "fee"                         NUMERIC            NOT NULL,
    "is_self_payment_fee"         BOOLEAN            NOT NULL,
    "status"                      TRANSACTION_STATUS NOT NULL,
    CONSTRAINT "transaction_pk" PRIMARY KEY ("id")
) INHERITS ("premiere_abstract_table") WITH (OIDS= FALSE);

CREATE TABLE "checking_transaction"
(
    "id"                          SERIAL NOT NULL,
    "amount"                      NUMERIC,
    "type"                        TRANSACTION_TYPE,
    "transaction_remark"          VARCHAR(255),
    "sender_balance"              NUMERIC,
    "receiver_balance"            NUMERIC,
    "sender_credit_card_number"   VARCHAR(255),
    "receiver_credit_card_number" VARCHAR(255),
    "sender_bank_id"              INTEGER,
    "receiver_bank_id"            INTEGER,
    "fee"                         NUMERIC,
    "is_self_payment_fee"         BOOLEAN,
    "is_internal"                 BOOLEAN,
    "status"                      TRANSACTION_STATUS,
    CONSTRAINT "checking_transaction_pk" PRIMARY KEY ("id")
) INHERITS ("premiere_abstract_table") WITH (OIDS= FALSE);

ALTER TABLE "credit_card"
    ADD CONSTRAINT "credit_card_fk0" FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "receiver"
    ADD CONSTRAINT "receivers_fk0" FOREIGN KEY ("user_id") REFERENCES "user" ("id");
ALTER TABLE "receiver"
    ADD CONSTRAINT "receivers_fk1" FOREIGN KEY ("bank_id") REFERENCES "bank" ("id");

ALTER TABLE "loan_reminder"
    ADD CONSTRAINT "loan_reminder_fk0" FOREIGN KEY ("sender_credit_card_id") REFERENCES "credit_card" ("id");
ALTER TABLE "loan_reminder"
    ADD CONSTRAINT "loan_reminder_fk1" FOREIGN KEY ("receiver_credit_card_id") REFERENCES "credit_card" ("id");

ALTER TABLE "transaction"
    ADD CONSTRAINT "transaction_fk1" FOREIGN KEY ("sender_bank_id") REFERENCES "bank" ("id");
ALTER TABLE "transaction"
    ADD CONSTRAINT "transaction_fk2" FOREIGN KEY ("receiver_bank_id") REFERENCES "bank" ("id");

ALTER TABLE "checking_transaction"
    ADD CONSTRAINT "checking_transaction_fk1" FOREIGN KEY ("sender_bank_id") REFERENCES "bank" ("id");

ALTER TABLE "checking_transaction"
    ADD CONSTRAINT "checking_transaction_fk2" FOREIGN KEY ("receiver_bank_id") REFERENCES "bank" ("id");