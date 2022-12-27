package org.hcmus.premiere.security.configuration;

import static java.util.Arrays.asList;
import static org.hcmus.premiere.model.enums.PremiereRole.CUSTOMER;
import static org.hcmus.premiere.model.enums.PremiereRole.EMPLOYEE;
import static org.hcmus.premiere.model.enums.PremiereRole.PREMIERE_ADMIN;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.common.consts.PremiereApiUrls;
import org.hcmus.premiere.security.filter.ApiSecretKeyFilter;
import org.hcmus.premiere.util.security.SecurityUtils;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@KeycloakConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@Import(KeycloakSpringBootConfigResolver.class)
public class SecurityConfiguration {

  @KeycloakConfiguration
  @Order(1)
  public static class InternalSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
      KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
      keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
      auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
      return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      super.configure(http);

      http.cors()
          .and()
          .csrf()
          .disable()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .antMatcher(PremiereApiUrls.PREMIERE_API_V1 + "/**")
          .authorizeRequests()
          .antMatchers(PremiereApiUrls.PREMIERE_API_V1 + "/credit-card/**")
          .hasAnyRole(CUSTOMER.value, EMPLOYEE.value, PREMIERE_ADMIN.value)
          .antMatchers(PremiereApiUrls.PREMIERE_API_V1 + "/receivers/**").hasRole(CUSTOMER.value)
          .anyRequest()
          .authenticated(); // used to be .permitAll()
    }
  }

  @KeycloakConfiguration
  @RequiredArgsConstructor
  public static class ExternalSecurityConfigurerAdapter extends KeycloakWebSecurityConfigurerAdapter {

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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(false);
    configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}