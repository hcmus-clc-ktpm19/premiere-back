package org.hcmus.premiere.util.security;

import java.util.Arrays;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("SecurityUtils")
public class SecurityUtils {

  private static final String PREFIX = "ROLE_";

  private SecurityUtils() {
  }

  public static boolean hasRole(PremiereRole role) {
    return SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getAuthorities()
        .stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(PREFIX + role));
  }

  public static boolean containsRoles(PremiereRole... roles) {
    return Arrays.stream(roles).anyMatch(SecurityUtils::hasRole);
  }
}
