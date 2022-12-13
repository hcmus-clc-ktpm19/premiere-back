CREATE SCHEMA premiere;
SET SEARCH_PATH TO premiere;

CREATE TYPE GENDER AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE LOAN_STATUS AS ENUM ('APPROVED', 'REJECTED', 'PENDING');
CREATE TYPE TRANSACTION_TYPE AS ENUM ('PURCHASE', 'REDEMPTION');
CREATE TYPE TRANSACTION_STATUS AS ENUM ('CHECKING', 'COMPLETED');

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
    "version"    INT          NOT NULL DEFAULT 0,
    CONSTRAINT "user_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

CREATE TABLE "credit_card"
(
    "id"          SERIAL       NOT NULL,
    "user_id"     INTEGER      NOT NULL UNIQUE,
    "balance"     NUMERIC      NOT NULL,
    "open_day"    TIMESTAMP    NOT NULL,
    "card_number" VARCHAR(255) NOT NULL UNIQUE,
    "version"     INT          NOT NULL DEFAULT 0,
    CONSTRAINT "credit_card_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

CREATE TABLE "bank"
(
    "id"        SERIAL       NOT NULL,
    "bank_name" VARCHAR(255) NOT NULL UNIQUE,
    "version"   INT          NOT NULL DEFAULT 0,
    CONSTRAINT "bank_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

CREATE TABLE "receiver"
(
    "id"          SERIAL       NOT NULL,
    "card_number" VARCHAR(255) NOT NULL,
    "nickname"    VARCHAR(255) NOT NULL,
    "full_name"   VARCHAR(255) NOT NULL,
    "user_id"     INTEGER      NOT NULL,
    "bank_id"     INTEGER      NOT NULL,
    "version"     INT          NOT NULL DEFAULT 0,
    CONSTRAINT "receivers_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

CREATE TABLE "loan_reminder"
(
    "id"                      SERIAL       NOT NULL,
    "loan_balance"            NUMERIC      NOT NULL,
    "sender_credit_card_id"   INTEGER      NOT NULL,
    "receiver_credit_card_id" INTEGER      NOT NULL,
    "status"                  LOAN_STATUS  NOT NULL,
    "time"                    TIMESTAMP    NOT NULL,
    "loan_remark"             VARCHAR(255) NOT NULL,
    "version"                 INT          NOT NULL DEFAULT 0,
    CONSTRAINT "loan_reminder_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

CREATE TABLE "transaction"
(
    "id"                          SERIAL             NOT NULL,
    "amount"                      NUMERIC            NOT NULL,
    "type"                        TRANSACTION_TYPE   NOT NULL,
    "time"                        TIMESTAMP          NOT NULL,
    "transaction_remark"          VARCHAR(255)       NOT NULL,
    "total_balance"               NUMERIC            NOT NULL,
    "sender_credit_card_number"   VARCHAR(255)       NOT NULL,
    "receiver_credit_card_number" VARCHAR(255)       NOT NULL,
    "sender_bank_id"              INTEGER            NOT NULL,
    "receiver_bank_id"            INTEGER            NOT NULL,
    "fee"                         NUMERIC            NOT NULL,
    "is_self_payment_fee"         BOOLEAN            NOT NULL,
    "status"                      TRANSACTION_STATUS NOT NULL,
    "version"                     INT                NOT NULL DEFAULT 0,
    CONSTRAINT "transaction_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );


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

INSERT INTO "user"
VALUES (1, 'Nam', 'Nguyen Duc', '01/01/2001', 'MALE', 'keke@gmail.com', '0987654321', 'pan_number',
        'address', 'avatar');
INSERT INTO "user"
VALUES (2, 'Giap', 'Hoang Huu', '01/01/2001', 'FEMALE', 'hehe@gmail.com', '0987654322',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"
VALUES (3, 'Nhat', 'Le Ngoc Minh', '01/01/2001', 'MALE', 'hihi@gmail.com', '0987654323',
        'pan_number', 'address', 'avatar');


INSERT INTO "bank"
VALUES (1, 'Vietcombank');
INSERT INTO "bank"
VALUES (2, 'Vietinbank');
INSERT INTO "bank"
VALUES (3, 'Techcombank');

INSERT INTO "credit_card"
VALUES (1, 2, 100000, CURRENT_TIMESTAMP, '1234567890123456', 1);

INSERT INTO "loan_reminder"
VALUES (1, 100000, 1, 2, 'APPROVED', CURRENT_TIMESTAMP, 'hehe');
INSERT INTO "loan_reminder"
VALUES (2, 100000, 2, 3, 'REJECTED', CURRENT_TIMESTAMP, 'hihi');
INSERT INTO "loan_reminder"
VALUES (3, 100000, 3, 1, 'PENDING', CURRENT_TIMESTAMP, 'hoho');

INSERT INTO "receiver"
VALUES (1, '1234567890123456', 'Nam', 'Nguyen Duc Nam', 1, 1);
INSERT INTO "receiver"
VALUES (2, '1234567890123457', 'Giap', 'Hoang Huu Giap', 2, 2);
INSERT INTO "receiver"
VALUES (3, '1234567890123456', 'Nhat', 'Le Ngoc Minh Nhat', 3, 3);