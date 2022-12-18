package org.hcmus.premiere.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sendinblue.ApiClient;
import sendinblue.auth.ApiKeyAuth;

@Configuration
@RequiredArgsConstructor
public class PremiereConfiguration {
  private final KeycloakSpringBootProperties keycloakSpringBootProperties;

  @Value("${sendinblue.api-key}")
  private String SENDINBLUE_API_KEY;

  @Value("${keycloak.client-key-password}")
  private String KEYCLOAK_SECRET_KEY;

  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder
        .builder()
        .realm(keycloakSpringBootProperties.getRealm())
        .serverUrl(keycloakSpringBootProperties.getAuthServerUrl())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(keycloakSpringBootProperties.getResource())
        .clientSecret(KEYCLOAK_SECRET_KEY)
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

  @Bean
  public ApiKeyAuth apiKeyAuth(){
    ApiClient defaultClient = sendinblue.Configuration.getDefaultApiClient();

    // Configure API key authorization: api-key
    ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
    apiKey.setApiKey(SENDINBLUE_API_KEY);
    return apiKey;
  }
}
