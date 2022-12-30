package org.hcmus.premiere.resource;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.hcmus.premiere.common.consts.PremiereApiUrls;

@Path(PremiereApiUrls.PREMIERE_API_V2_EXTERNAL)
public interface ExternalBankResource {

  @GET
  @Path("/banks/credit-cards")
  @Produces({ MediaType.APPLICATION_JSON })
  List<Object> getCreditCardsFromByExternalBankId(
      @HeaderParam("X-Auth-Token") String authToken,
      @HeaderParam("X-Auth-Time") String authTime,
      @HeaderParam("X-Auth-Locale") String authLocale
  );

  @GET
  @Path("/banks/credit-cards/{creditCardNumber}")
  @Produces({ MediaType.APPLICATION_JSON })
  Object getCreditCardByNumberAndExternalBankId(
      @HeaderParam("X-Auth-Token") String authToken,
      @HeaderParam("X-Auth-Time") String authTime,
      @HeaderParam("X-Auth-Locale") String authLocale,
      @PathParam("creditCardNumber") String creditCardNumber
  );
}
