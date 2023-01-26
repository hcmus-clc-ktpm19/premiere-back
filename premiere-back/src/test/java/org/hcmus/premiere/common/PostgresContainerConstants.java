package org.hcmus.premiere.common;

public class PostgresContainerConstants {

  public static final String POSTGRES_CONTAINER_IMAGE = "postgres:15.1-alpine3.17";
  public static final String POSTGRES_DATABASE_NAME = "PREMIERE";
  public static final String POSTGRES_USERNAME = "admin";
  public static final String POSTGRES_PASSWORD = "admin";
  public static final int POSTGRES_PORT = 5432;
  public static final String DB_SCRIPTS_PATH = "src/test/resources/db";
  public static final String ENTRYPOINT_INIT_DB_PATH = "/docker-entrypoint-initdb.d";
}
