SET SEARCH_PATH TO premiere;

INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Nam', 'Nguyen Duc', '01/01/2001', 'MALE', 'keke@gmail.com', '0987654321', 'pan_number',
        'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Giap', 'Hoang Huu', '01/01/2001', 'FEMALE', 'hehe@gmail.com', '0987654322',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Nhat', 'Le Ngoc Minh', '01/01/2001', 'MALE', 'hihi@gmail.com', '0987654323',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Huy', 'Nguyen Van', '01/01/2001', 'MALE', 'huhu@gmail.com', '0987654324',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Hai', 'Nguyen Van', '01/01/2001', 'MALE','test1@gmail.com', '0987654325',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Ba', 'Nguyen Van', '01/01/2001', 'MALE','test2@gmail.com', '0987654326',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Bon', 'Nguyen Van', '01/01/2001', 'MALE','test3@gmail.com', '0987654327',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Nam', 'Nguyen Van', '01/01/2001', 'MALE','test4@gmail.com', '0987654328',
        'pan_number', 'address', 'avatar');

INSERT INTO "credit_card"(user_id, balance, open_day, card_number)
VALUES (1, 100000, CURRENT_TIMESTAMP, '1234567890123451');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number)
VALUES (2, 200000, CURRENT_TIMESTAMP, '1234567890123452');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number)
VALUES (3, 300000, CURRENT_TIMESTAMP, '1234567890123453');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number)
VALUES (4, 400000, CURRENT_TIMESTAMP, '1234567890123454');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number)
VALUES (5, 500000, CURRENT_TIMESTAMP, '1234567890123455');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number)
VALUES (6, 600000, CURRENT_TIMESTAMP, '1234567890123456');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number)
VALUES (7, 700000, CURRENT_TIMESTAMP, '1234567890123457');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number)
VALUES (8, 800000, CURRENT_TIMESTAMP, '1234567890123458');

INSERT INTO "bank"(bank_name)
VALUES ('Premierebank');
INSERT INTO "bank"(bank_name)
VALUES ('Vietcombank');
INSERT INTO "bank"(bank_name)
VALUES ('Vietinbank');
INSERT INTO "bank"(bank_name)
VALUES ('Techcombank');

INSERT INTO "loan_reminder"(loan_balance, sender_credit_card_id, receiver_credit_card_id, status,
                            time, loan_remark)
VALUES (100000, 1, 2, 'APPROVED', CURRENT_TIMESTAMP, 'hehe');

INSERT INTO "loan_reminder"(loan_balance, sender_credit_card_id, receiver_credit_card_id, status,
                            time, loan_remark)
VALUES (120000, 2, 1, 'PENDING', CURRENT_TIMESTAMP, 'tra tien cho toi');

INSERT INTO "receiver"(card_number, nickname, full_name, user_id, bank_id)
VALUES ('1234567890123456', 'Nam', 'Nguyen Duc Nam', 2, 1);
INSERT INTO "receiver"(card_number, nickname, full_name, user_id, bank_id)
VALUES ('1234567890123457', 'Giap', 'Hoang Huu Giap', 1, 2);
INSERT INTO "receiver"(card_number, nickname, full_name, user_id, bank_id)
VALUES ('1234567890123456', 'Nhat', 'Le Ngoc Minh Nhat', 1, 3);