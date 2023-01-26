package org.hcmus.premiere.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PostgresContainerConstants {

  public final String POSTGRES_CONTAINER_IMAGE = "postgres:15.1-alpine3.17";
  public final String POSTGRES_DATABASE_NAME = "PREMIERE";
  public final String POSTGRES_USERNAME = "admin";
  public final String POSTGRES_PASSWORD = "admin";
  public final int POSTGRES_PORT = 5432;
  public final String DB_SCRIPTS_PATH = "src/test/resources/db";
  public final String ENTRYPOINT_INIT_DB_PATH = "/docker-entrypoint-initdb.d";
}
