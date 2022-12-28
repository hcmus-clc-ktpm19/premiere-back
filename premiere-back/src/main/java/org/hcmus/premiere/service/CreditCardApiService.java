package org.hcmus.premiere.service;

import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.hcmus.premiere.common.consts.PremiereApiUrls;

@Path(PremiereApiUrls.PREMIERE_API_V2_EXTERNAL)
public interface CreditCardApiService {

  @GET
  @Path("/banks/credit-cards")
  @Produces({ MediaType.APPLICATION_JSON })
  List<Map<String, String>> getCreditCardsFromByExternalBankId();
}
