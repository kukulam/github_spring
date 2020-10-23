package com.kukulam.githubspring.infrastracture.github;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Optional;

@Component
public class GithubClient {

    private static final Logger logger = LoggerFactory.getLogger(GithubClient.class);

    private final RestTemplate restTemplate;
    private final GithubSettings githubSettings;

    private final RetryConfig retryConfig = RetryConfig.custom()
            .maxAttempts(3)
            .waitDuration(Duration.ofSeconds(1))
            .ignoreExceptions(HttpClientErrorException.class)
            .build();
    private final Retry retry = Retry.of("github", retryConfig);

    public GithubClient(RestTemplate restTemplate, GithubSettings githubSettings) {
        this.restTemplate = restTemplate;
        this.githubSettings = githubSettings;
    }

    public Optional<GithubRepository> fetchRepositoryInfo(String owner, String name) {
        String uri = UriComponentsBuilder.fromHttpUrl(githubSettings.getUrl())
                .path("/repos")
                .pathSegment(owner)
                .pathSegment(name)
                .toUriString();

        try {
            var response = retry.executeCallable(() -> {
                logger.info("Url to github: " + uri);
                return restTemplate.getForEntity(uri, GithubRepository.class);
            });
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

