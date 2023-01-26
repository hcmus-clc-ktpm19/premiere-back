SET SEARCH_PATH TO premiere;

UPDATE "checking_transaction" SET "created_at" = NOW() - INTERVAL '1 DAY' * id, "updated_at" = NOW() + INTERVAL '1 DAY' * id;

UPDATE "transaction" SET "created_at" = NOW() - INTERVAL '1 DAY' * id, "updated_at" = NOW() + INTERVAL '1 DAY' * id;