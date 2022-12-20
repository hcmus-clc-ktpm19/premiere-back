package org.hcmus.premiere.model.dto;

import lombok.Data;

@Data
public class PaginationMetaDataDto {

  private long currPage;
  private long sizePerPage;
  private long currPageTotalElements;
  private long totalPages;
  private boolean first;
  private boolean last;
}
