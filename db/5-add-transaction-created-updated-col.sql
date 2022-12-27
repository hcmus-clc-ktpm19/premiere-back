SET SEARCH_PATH TO premiere;

ALTER TABLE "checking_transaction" ADD COLUMN "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE "checking_transaction" ADD COLUMN "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE "checking_transaction" SET "created_at" = NOW() - INTERVAL '1 DAY' * id, "updated_at" = NOW() + INTERVAL '1 DAY' * id;

ALTER TABLE "transaction" ADD COLUMN "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE "transaction" ADD COLUMN "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE "transaction" SET "created_at" = NOW() - INTERVAL '1 DAY' * id, "updated_at" = NOW() + INTERVAL '1 DAY' * id;