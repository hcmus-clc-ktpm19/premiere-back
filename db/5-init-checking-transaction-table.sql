SET SEARCH_PATH TO premiere;

ALTER TABLE "checking_transaction"
    ADD CONSTRAINT "checking_transaction_fk1" FOREIGN KEY ("sender_bank_id") REFERENCES "bank" ("id");

ALTER TABLE "checking_transaction"
    ADD CONSTRAINT "checking_transaction_fk2" FOREIGN KEY ("receiver_bank_id") REFERENCES "bank" ("id");
CREATE TABLE "checking_transaction"
(
    "id"                          SERIAL NOT NULL,
    "amount"                      NUMERIC,
    "type"                        TRANSACTION_TYPE,
    "time"                        TIMESTAMP,
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
    "version"                     INT DEFAULT 0,
    CONSTRAINT "checking_transaction_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );
