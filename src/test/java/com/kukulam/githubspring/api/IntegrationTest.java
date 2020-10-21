package com.kukulam.githubspring.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(
        properties = { "application.environment=integration" },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
public class IntegrationTest {

    @LocalServerPort
    private int port;

    protected RestTemplate restTemplate = new RestTemplate();

    protected String baseUrl() {
        return "http://localhost:" + port;
    }
}

