package org.hcmus.premiere.security.filter;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Slf4j
public class ApiSecretKeyFilter extends AbstractPreAuthenticatedProcessingFilter {

  private final String principalRequestHeader;

  private final String timeRequestHeader;

  private final String localeRequestHeader;

  public ApiSecretKeyFilter(String principalRequestHeader, String timeRequestHeader,
      String localeRequestHeader) {
    this.principalRequestHeader = principalRequestHeader;
    this.timeRequestHeader = timeRequestHeader;
    this.localeRequestHeader = localeRequestHeader;
  }

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    return request.getHeader(principalRequestHeader);
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return new ArrayList<>(List.of(request.getServletPath(), request.getHeader(timeRequestHeader), request.getHeader(localeRequestHeader)));
  }
}