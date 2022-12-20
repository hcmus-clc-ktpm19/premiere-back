SET SEARCH_PATH TO premiere;

INSERT INTO "transaction"(id, amount, time, type, transaction_remark, total_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (1, 100000, CURRENT_TIMESTAMP, 'MONEY_TRANSFER', 'HEHE', 101000, '1234567890123456',
        '1234567890123456', 1, 1, 1000, TRUE, 'CHECKING');

INSERT INTO "transaction"(id, amount, time, type, transaction_remark, total_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (2, 200000, CURRENT_TIMESTAMP, 'MONEY_TRANSFER', 'HIHI', 202000, '1234567890123456',
        '1234567890123456', 2, 2, 2000, TRUE, 'CHECKING');