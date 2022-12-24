package org.hcmus.premiere.service.impl;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hcmus.premiere.common.Constants;
import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.service.BankService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.ValidationService;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class ValidationServiceImpl implements ValidationService {

  private final CreditCardService creditCardService;

  private final BankService bankService;

  @Override
  public Boolean validateTransactionRequest(TransactionRequestDto transactionRequestDto) {
    BigDecimal amount = new BigDecimal(transactionRequestDto.getAmount());
    if (amount.compareTo(Constants.MINIMUM_AMOUNT_TO_WITHDRAW) < 0) {
      throw new IllegalArgumentException(Constants.THE_TRANSFER_AMOUNT_IS_NOT_VALID);
    }
    BigDecimal totalAmount;
    if (transactionRequestDto.getIsSelfPaymentFee()) {
      totalAmount = amount.add(amount.multiply(Constants.TRANSACTION_FEE));
    } else {
      totalAmount = amount;
    }
    CreditCard senderCard = creditCardService.findCreditCardByNumber(transactionRequestDto.getSenderCardNumber());
    if (senderCard.getBalance().subtract(totalAmount).compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(Constants.ACCOUNT_CURRENT_BALANCE_IS_NOT_AVAILABLE_TO_WITHDRAW);
    }

    if (transactionRequestDto.getIsInternal()) {
      creditCardService.findCreditCardByNumber(transactionRequestDto.getReceiverCardNumber());
    } else {
      if(StringUtils.isEmpty(transactionRequestDto.getReceiverBankName())) {
        throw new IllegalArgumentException(Constants.BANK_NAME_IS_NOT_VALID);
      }
      bankService.findBankByName(transactionRequestDto.getReceiverBankName());
      // TODO: validate receiver card number from other bank (rest template)
    }
    return true;
  }

}
