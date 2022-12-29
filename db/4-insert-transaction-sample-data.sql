SET SEARCH_PATH TO premiere;

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (100000, 'MONEY_TRANSFER', 'HEHE', 101000, 101000, '1234567890123456',
        '1234567890123456', 1, 1, 1000, TRUE, 'CHECKING');

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (200000, 'MONEY_TRANSFER', 'HIHI', 202000, 202000, '1234567890123456',
        '1234567890123456', 2, 2, 2000, TRUE, 'CHECKING');

----------------------------------------------------------------------------------------------------

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (200000, 'LOAN', 'HIHI', 202000, 202000, '1234567890123457',
        '1234567890123456', 2, 2, 2000, TRUE, 'CHECKING');

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (200000, 'MONEY_TRANSFER', 'HIHI', 202000, 202000, '1234567890123457',
        '1234567890123457', 2, 2, 2000, TRUE, 'CHECKING');