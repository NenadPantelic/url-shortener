package org.pisibp.demo.urlshortener.repository;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
public class DatabaseSetup {

    @Container
    protected static PostgreSQLContainer container = new PostgreSQLContainer("postgres:13.0")
            .withDatabaseName("test_db")
            .withUsername("Test")
            .withPassword("Test");

    @BeforeAll
    public static void setUp() {
        container.withReuse(true);
        container.withInitScript("src/main/resources/db/init-db.sql");
        container.start();
    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @AfterAll
    public static void tearDown() {
        container.stop();
    }
}