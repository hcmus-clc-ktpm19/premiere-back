SET SEARCH_PATH TO premiere;

INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Nam', 'Nguyen Duc', '01/01/2001', 'MALE', 'customer1@gmail.com', '0987654321', 'pan_number',
        'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Giap', 'Hoang Huu', '01/01/2001', 'FEMALE', 'customer2@gmail.com', '0987654322',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Nhat', 'Le Ngoc Minh', '01/01/2001', 'MALE', 'customer3@gmail.com', '0987654323',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Huy', 'Nguyen Van', '01/01/2001', 'MALE', 'customer4@gmail.com', '0987654324',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Hai', 'Nguyen Van', '01/01/2001', 'MALE','customer5@gmail.com', '0987654325',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Ba', 'Nguyen Van', '01/01/2001', 'MALE','customer6@gmail.com', '0987654326',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Bon', 'Nguyen Van', '01/01/2001', 'MALE','customer7@gmail.com', '0987654327',
        'pan_number', 'address', 'avatar');
INSERT INTO "user"(first_name, last_name, dob, gender, email, phone, pan_number, address, avatar)
VALUES ('Nam', 'Nguyen Van', '01/01/2001', 'MALE','customer8@gmail.com', '0987654328',
        'pan_number', 'address', 'avatar');

INSERT INTO "credit_card"(user_id, balance, open_day, card_number, status)
VALUES (1, 100000, CURRENT_TIMESTAMP, '1234567890123451', 'AVAILABLE');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number, status)
VALUES (2, 200000, CURRENT_TIMESTAMP, '1234567890123452', 'AVAILABLE');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number, status)
VALUES (3, 300000, CURRENT_TIMESTAMP, '1234567890123453', 'AVAILABLE');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number, status)
VALUES (4, 400000, CURRENT_TIMESTAMP, '1234567890123454', 'AVAILABLE');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number, status)
VALUES (5, 500000, CURRENT_TIMESTAMP, '1234567890123455', 'AVAILABLE');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number, status)
VALUES (6, 600000, CURRENT_TIMESTAMP, '1234567890123456', 'AVAILABLE');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number, status)
VALUES (7, 700000, CURRENT_TIMESTAMP, '1234567890123457', 'AVAILABLE');
INSERT INTO "credit_card"(user_id, balance, open_day, card_number, status)
VALUES (8, 800000, CURRENT_TIMESTAMP, '1234567890123458', 'AVAILABLE');

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
VALUES ('1234567890123458', 'Nhat', 'Le Ngoc Minh Nhat', 1, 3);