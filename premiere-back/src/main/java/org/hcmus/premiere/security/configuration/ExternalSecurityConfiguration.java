package org.hcmus.premiere.security.configuration;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.common.consts.PremiereApiUrls;
import org.hcmus.premiere.security.filter.ApiSecretKeyFilter;
import org.hcmus.premiere.util.security.SecurityUtils;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@Import(KeycloakSpringBootConfigResolver.class)
@RequiredArgsConstructor
public class ExternalSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

  @Value("${system-auth.http.auth-header-name}")
  private String principalRequestHeader;

  @Value("${system-auth.http.time-header-name}")
  private String timeRequestHeader;

  @Value("${system-auth.http.locale-header-name}")
  private String localeRequestHeader;

  @Value("${system-auth.secret-key}")
  private String secretKey;

  private final SecurityUtils securityUtils;

  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.cors()
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(apiSecretKeyFilter())
        .antMatcher(PremiereApiUrls.PREMIERE_API_V2_EXTERNAL + "/**")
        .authorizeRequests()
        .anyRequest()
        .authenticated();
  }

  @Bean
  public ApiSecretKeyFilter apiSecretKeyFilter() {
    ApiSecretKeyFilter apiSecretKeyFilter = new ApiSecretKeyFilter(principalRequestHeader,
        timeRequestHeader, localeRequestHeader);
    apiSecretKeyFilter.setAuthenticationManager(authentication -> {
      List<String> credentials = (List<String>) authentication.getCredentials();
      String servletPath = credentials.get(0);
      LocalDateTime credentialsTime = LocalDateTime.parse(credentials.get(1));
      ZoneId zoneId = ZoneId.of(credentials.get(2));

      String checksum = (String) authentication.getPrincipal();
      if (LocalDateTime.now(zoneId).isAfter(credentialsTime.plusMinutes(1))
          || !checksum.equals(securityUtils.hash(servletPath + credentialsTime + zoneId + secretKey))) {
        throw new AccessDeniedException("Time expired or checksum is invalid");
      }

      authentication.setAuthenticated(true);
      return authentication;
    });

    return apiSecretKeyFilter;
  }
}