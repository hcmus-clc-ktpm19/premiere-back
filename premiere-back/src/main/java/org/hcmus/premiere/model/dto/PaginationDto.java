package org.hcmus.premiere.model.dto;

import lombok.Data;

@Data
public class PaginationDto {

  private long currPage;
  private long sizePerPage;
  private long currPageTotalElements;
  private long totalElements;
  private long totalPages;
  private boolean first;
  private boolean last;
}
