package org.hcmus.premiere.util.test.container;

import static org.hcmus.premiere.common.PostgresContainerConstants.DB_SCRIPTS_PATH;
import static org.hcmus.premiere.common.PostgresContainerConstants.ENTRYPOINT_INIT_DB_PATH;
import static org.hcmus.premiere.common.PostgresContainerConstants.POSTGRES_CONTAINER_IMAGE;
import static org.hcmus.premiere.common.PostgresContainerConstants.POSTGRES_DATABASE_NAME;
import static org.hcmus.premiere.common.PostgresContainerConstants.POSTGRES_PASSWORD;
import static org.hcmus.premiere.common.PostgresContainerConstants.POSTGRES_PORT;
import static org.hcmus.premiere.common.PostgresContainerConstants.POSTGRES_USERNAME;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresContainerExtension implements BeforeAllCallback {

  @Container
  private final PostgreSQLContainer<?> postgreSQLContainer
      = (PostgreSQLContainer<?>) new PostgreSQLContainer(
      POSTGRES_CONTAINER_IMAGE)
      .withDatabaseName(POSTGRES_DATABASE_NAME)
      .withUsername(POSTGRES_USERNAME)
      .withPassword(POSTGRES_PASSWORD)
      .withExposedPorts(POSTGRES_PORT)
      .withFileSystemBind(DB_SCRIPTS_PATH, ENTRYPOINT_INIT_DB_PATH, BindMode.READ_ONLY);

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    postgreSQLContainer.start();
    overrideProperties();
  }

  private void overrideProperties() {
    System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl() + "&stringtype=unspecified");
    System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
    System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
  }
}
