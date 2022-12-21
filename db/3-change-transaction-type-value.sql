SET SEARCH_PATH TO premiere;

DROP TYPE TRANSACTION_TYPE CASCADE;
CREATE TYPE TRANSACTION_TYPE AS ENUM ('MONEY_TRANSFER', 'LOAN');
ALTER TABLE "transaction" ADD COLUMN "type" TRANSACTION_TYPE NOT NULL;