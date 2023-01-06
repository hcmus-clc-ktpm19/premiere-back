SET SEARCH_PATH TO premiere;

--     USER 1
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 100000, 200000, '1234567890123451',
        '1234567890123452', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 90000, 300000, '1234567890123451',
        '1234567890123453', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 80000, 400000, '1234567890123451',
        '1234567890123454', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 70000, 500000, '1234567890123451',
        '1234567890123455', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 60000, 600000, '1234567890123451',
        '1234567890123466', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 50000, 700000, '1234567890123451',
        '1234567890123457', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 40000, 800000, '1234567890123451',
        '1234567890123458', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 30000, 210000, '1234567890123451',
        '1234567890123452', 1, 1, 1000, FALSE, 'CHECKING');

--     USER 2---------------------------------------------------------------------------------------------------

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 220000, 30000, '1234567890123452',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 210000, 300000, '1234567890123452',
        '1234567890123453', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 200000, 400000, '1234567890123452',
        '1234567890123454', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 190000, 500000, '1234567890123452',
        '1234567890123455', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 180000, 600000, '1234567890123452',
        '1234567890123456', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 170000, 700000, '1234567890123452',
        '1234567890123457', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 160000, 800000, '1234567890123452',
        '1234567890123458', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 150000, 40000, '1234567890123452',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');

--     USER 3---------------------------------------------------------------------------------------------------

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 220000, 30000, '1234567890123453',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 210000, 300000, '1234567890123453',
        '1234567890123452', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 200000, 400000, '1234567890123453',
        '1234567890123454', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 190000, 500000, '1234567890123453',
        '1234567890123455', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 180000, 600000, '1234567890123453',
        '1234567890123456', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 170000, 700000, '1234567890123453',
        '1234567890123457', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 160000, 800000, '1234567890123453',
        '1234567890123458', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 150000, 40000, '1234567890123453',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');

--     USER 4---------------------------------------------------------------------------------------------------

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 220000, 30000, '1234567890123454',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 210000, 300000, '1234567890123454',
        '1234567890123452', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 200000, 400000, '1234567890123454',
        '1234567890123453', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 190000, 500000, '1234567890123454',
        '1234567890123454', 1, 1, 1000, TRUE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 180000, 600000, '1234567890123454',
        '1234567890123455', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 170000, 700000, '1234567890123454',
        '1234567890123456', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 160000, 800000, '1234567890123454',
        '1234567890123457', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 150000, 40000, '1234567890123454',
        '1234567890123458', 1, 1, 1000, FALSE, 'CHECKING');

--     USER 5---------------------------------------------------------------------------------------------------

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 220000, 30000, '1234567890123455',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 210000, 300000, '1234567890123455',
        '1234567890123452', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 200000, 400000, '1234567890123455',
        '1234567890123453', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 190000, 500000, '1234567890123455',
        '1234567890123454', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 180000, 600000, '1234567890123455',
        '1234567890123455', 1, 1, 1000, TRUE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 170000, 700000, '1234567890123455',
        '1234567890123456', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 160000, 800000, '1234567890123455',
        '1234567890123457', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 150000, 40000, '1234567890123455',
        '1234567890123458', 1, 1, 1000, FALSE, 'CHECKING');

--     USER 6---------------------------------------------------------------------------------------------------

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 220000, 30000, '1234567890123456',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 210000, 300000, '1234567890123456',
        '1234567890123452', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 200000, 400000, '1234567890123456',
        '1234567890123453', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 190000, 500000, '1234567890123456',
        '1234567890123454', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 180000, 600000, '1234567890123456',
        '1234567890123455', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 170000, 700000, '1234567890123456',
        '1234567890123456', 1, 1, 1000, TRUE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 160000, 800000, '1234567890123456',
        '1234567890123457', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 150000, 40000, '1234567890123456',
        '1234567890123458', 1, 1, 1000, FALSE, 'CHECKING');

--     USER 7---------------------------------------------------------------------------------------------------

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 220000, 30000, '1234567890123457',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 210000, 300000, '1234567890123457',
        '1234567890123452', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 200000, 400000, '1234567890123457',
        '1234567890123453', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 190000, 500000, '1234567890123457',
        '1234567890123454', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 180000, 600000, '1234567890123457',
        '1234567890123455', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 170000, 700000, '1234567890123457',
        '1234567890123456', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 160000, 800000, '1234567890123457',
        '1234567890123457', 1, 1, 1000, TRUE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 150000, 40000, '1234567890123457',
        '1234567890123458', 1, 1, 1000, FALSE, 'CHECKING');

--     USER 8---------------------------------------------------------------------------------------------------

INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 220000, 30000, '1234567890123458',
        '1234567890123451', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 210000, 300000, '1234567890123458',
        '1234567890123452', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 200000, 400000, '1234567890123458',
        '1234567890123453', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 190000, 500000, '1234567890123458',
        '1234567890123454', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 180000, 600000, '1234567890123458',
        '1234567890123455', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 170000, 700000, '1234567890123458',
        '1234567890123456', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 160000, 800000, '1234567890123458',
        '1234567890123457', 1, 1, 1000, FALSE, 'CHECKING');
INSERT INTO "transaction"(amount, type, transaction_remark, sender_balance, receiver_balance,
                          sender_credit_card_number, receiver_credit_card_number, sender_bank_id,
                          receiver_bank_id, fee, is_self_payment_fee, status)
VALUES (10000, 'MONEY_TRANSFER', 'hehe', 150000, 40000, '1234567890123458',
        '1234567890123458', 1, 1, 1000, TRUE, 'CHECKING');