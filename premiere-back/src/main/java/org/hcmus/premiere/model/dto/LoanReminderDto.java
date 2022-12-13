package org.hcmus.premiere.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hcmus.premiere.model.enums.LoanStatus;

/**
 * A DTO for the {@link org.hcmus.premiere.model.entity.LoanReminder} entity
 */
public record LoanReminderDto(Long id, Long version, BigDecimal loanBalance, LoanStatus status,
                              LocalDateTime time, String loanRemark) implements Serializable {

}