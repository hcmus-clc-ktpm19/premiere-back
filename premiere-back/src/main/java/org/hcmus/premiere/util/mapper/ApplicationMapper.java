package org.hcmus.premiere.util.mapper;

import java.util.List;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.dto.MetaDataDto;
import org.hcmus.premiere.model.dto.OTPDto;
import org.hcmus.premiere.model.dto.PaginationDto;
import org.hcmus.premiere.model.dto.PremierePaginationReponseDto;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.dto.TransactionCriteriaDto;
import org.hcmus.premiere.model.dto.TransactionDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.OTP;
import org.hcmus.premiere.model.entity.Receiver;
import org.springframework.stereotype.Component;


@Component
public class ApplicationMapper {

  public CreditCardDto toCreditCardDto(CreditCard creditCard) {
    if (creditCard == null) {
      return null;
    } else {
      CreditCardDto creditCardDto = new CreditCardDto();
      creditCardDto.setCardNumber(creditCard.getCardNumber());
      creditCardDto.setBalance(creditCard.getBalance());
      return creditCardDto;
    }
  }

  public ReceiverDto toReceiverDto(Receiver receiver) {
    if (receiver == null) {
      return null;
    } else {
      ReceiverDto receiverDto = new ReceiverDto();
      receiverDto.setId(receiver.getId());
      receiverDto.setCardNumber(receiver.getCardNumber());
      receiverDto.setNickname(receiver.getNickname());
      receiverDto.setFullName(receiver.getFullName());
      receiverDto.setUserId(receiver.getUser().getId());
      receiverDto.setBankName(receiver.getBank().getBankName());
      return receiverDto;
    }
  }

  public OTPDto toOTPDto(OTP otp){
    if (otp == null) {
      return null;
    } else {
      OTPDto otpDto = new OTPDto();
      otpDto.setOtp(otp.getOtp());
      otpDto.setEmail(otp.getEmail());
      otpDto.setCreatedAt(otp.getCreatedAt());
      return otpDto;
    }
  }

  public PremierePaginationReponseDto<TransactionDto> toDto(List<TransactionDto> transactionDtos,
      TransactionCriteriaDto criteriaDto) {
    PaginationDto paginationDto = new PaginationDto();
    paginationDto.setCurrPage(criteriaDto.getPage());
    paginationDto.setSizePerPage(criteriaDto.getSize());
    paginationDto.setCurrPageTotalElements(transactionDtos.size());
    paginationDto.setFirst(criteriaDto.getPage() == 0);
    paginationDto.setLast(criteriaDto.getPage() == paginationDto.getTotalPages());

    MetaDataDto metaDataDto = new MetaDataDto();
    metaDataDto.setPagination(paginationDto);

    return new PremierePaginationReponseDto<>(transactionDtos, metaDataDto);
  }
}
