package org.hcmus.premiere.model.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.util.validation.ValueOfEnum;
import javax.validation.constraints.NotBlank;


@Data
public class TransactionRequestDto {
  @NotBlank(message = "senderCardNumber is mandatory")
  private String senderCardNumber;
  @NotBlank(message = "receiverCardNumber is mandatory")
  private String receiverCardNumber;
  @NotBlank(message = "amount is mandatory")
  private String amount;
  @ValueOfEnum(enumClass = TransactionType.class)
  @NotBlank(message = "type is mandatory")
  private String type;
  @NotNull(message = "isInternal is mandatory")
  private Boolean isInternal;
  @NotNull(message = "isSelfPaymentFee is mandatory")
  private Boolean isSelfPaymentFee;
  @NotBlank(message = "remark is mandatory")
  private String remark;
  private String senderBankName;
  private String receiverBankName;
}

