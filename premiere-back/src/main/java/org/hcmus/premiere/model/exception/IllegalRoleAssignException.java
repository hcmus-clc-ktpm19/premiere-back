package org.hcmus.premiere.model.exception;

public class IllegalRoleAssignException extends PremiereAbstractException {

  public static final String ASSIGN_ILLEGAL_EMPLOYEE_ROLE = "AUTH.ROLE.ASSIGN_ILLEGAL_EMPLOYEE_ROLE";
  public static final String ASSIGN_ILLEGAL_CUSTOMER_ROLE = "AUTH.ROLE.ASSIGN_ILLEGAL_CUSTOMER_ROLE";

  public IllegalRoleAssignException(String message, String i18nPlaceHolder) {
    super(message, i18nPlaceHolder);
  }
}
