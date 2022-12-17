package org.hcmus.premiere.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class PremiereConfiguration {
  private final KeycloakSpringBootProperties keycloakSpringBootProperties;

  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder
        .builder()
        .realm(keycloakSpringBootProperties.getRealm())
        .serverUrl(keycloakSpringBootProperties.getAuthServerUrl())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(keycloakSpringBootProperties.getResource())
        .clientSecret(keycloakSpringBootProperties.getCredentials().get("secret").toString())
        .username("admin")
        .password("admin")
        .build();
  }

  @Bean
  public RealmResource realmResource() {
    return keycloak().realm(keycloakSpringBootProperties.getRealm());
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
