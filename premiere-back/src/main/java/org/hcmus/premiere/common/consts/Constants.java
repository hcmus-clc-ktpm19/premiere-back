package org.hcmus.premiere.common.consts;

import java.math.BigDecimal;

public class Constants {
    public static final String CREDIT_CARD_NUMBER_BIN = "2102";
    public static final int CREDIT_CARD_NUMBER_LENGTH = 16;
    public static final BigDecimal CREDIT_CARD_INITIAL_BALANCE = new BigDecimal(50000);
    public static final String KEYCLOAK_TOKEN_URL = "http://localhost:8180/realms/premiere-realm/protocol/openid-connect/token";
    public static final BigDecimal MINIMUM_AMOUNT_TO_WITHDRAW = new BigDecimal(10000);
    public static final BigDecimal TRANSACTION_FEE = new BigDecimal("0.01");
    public static final String PREMIERE_BANK_NAME = "Premierebank";
    public static final String TAIXIU_BANK_NAME = "Taixiubank";
    public static final String TRANSFER_SUCCESSFUL = "Transfer successful";
    public static final String TRANSFER_VALIDATE_SUCCESSFUL = "The validation of the transfer money request was successful. Please confirm the OTP code to complete the transaction";
    public static final String TRANSFER_VALIDATE_FAILED = "The validation of the transfer money request failed, please try again";
    public static final String MONEY_TRANSFER_TRANSACTION_FAILED_PLEASE_TRY_AGAIN = "Money transfer transaction failed, please try again";
    public static final String THE_TRANSFER_AMOUNT_IS_NOT_VALID= "The transfer amount is not valid";
    public static final String ACCOUNT_CURRENT_BALANCE_IS_NOT_AVAILABLE_TO_WITHDRAW = "The sender's account balance is not enough for the transaction";
    public static final String BANK_NAME_IS_NOT_VALID = "The name of the bank is invalid";
    public static final String OTP_IS_NOT_VALID = "The OTP is not valid";
    public static final String SENDER_AND_RECEIVER_ARE_SAME = "Both the sender and the receiver must be unique";
    public static final String TRANSACTION_ALREADY_COMPLETED = "Transaction has been completed";
    public static final String LOAN_REMINDER_STATUS_IS_NOT_PENDING = "The loan reminder status is not pending";
    public static final String INVALID_RSA_TOKEN = "The RSA token is invalid";
    public static final String EXTERNAL_BANK_ERROR = "The external bank is not available";
    public static final String PREMIERE_SLUG = "PREMIEREBANK";
    public static final String PAY_TRANSACTION_FEE_EXTERNAL_SRC = "SRC";
    public static final String BANK_NAME_NOT_FOUND = "The bank name is not found, or it did not connect to the external bank";
    }
