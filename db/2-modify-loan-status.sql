ALTER TABLE "loan_reminder" DROP COLUMN "status";
ALTER TABLE "loan_reminder" ADD COLUMN "status" VARCHAR(20) CHECK ( status IN ('APPROVED', 'REJECTED', 'PENDING') ) NOT NULL;