package org.hcmus.premiere.common;

import java.math.BigDecimal;

public class Constants {
    public static final String CREDIT_CARD_NUMBER_BIN = "2102";
    public static final int CREDIT_CARD_NUMBER_LENGTH = 12;
    public static final BigDecimal CREDIT_CARD_INITIAL_BALANCE = new BigDecimal(50000);
    public static final String KEYCLOAK_TOKEN_URL = "http://localhost:8180/realms/premiere-realm/protocol/openid-connect/token";

}
