package com.kukulam.githubspring.infrastracture.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class GithubClient {

    private static final Logger logger = LoggerFactory.getLogger(GithubClient.class);
    private static final String GITHUB_URL = "https://api.github.com";

    private final RestTemplate restTemplate;

    GithubClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GithubRepository fetchRepositoryInfo(String owner, String name) {
        String uri = UriComponentsBuilder.fromHttpUrl(GITHUB_URL)
                .path("/repos")
                .pathSegment(owner)
                .pathSegment(name)
                .toUriString();

        logger.info("Url to github: " + uri);

        try {
            var response = restTemplate.getForEntity(uri, GithubRepository.class);
            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new RuntimeException("No body"));
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundUserRepositoryException();
        }
    }
}

