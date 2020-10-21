package com.kukulam.githubspring.infrastracture.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class GithubClient {

    private static final Logger logger = LoggerFactory.getLogger(GithubClient.class);

    private final RestTemplate restTemplate;
    private final GithubSettings githubSettings;

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

        logger.info("Url to github: " + uri);

        try {
            var response = restTemplate.getForEntity(uri, GithubRepository.class);
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}

