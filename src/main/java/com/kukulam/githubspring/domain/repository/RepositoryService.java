package com.kukulam.githubspring.domain.repository;

import com.kukulam.githubspring.infrastracture.github.GithubClient;
import org.springframework.stereotype.Component;

@Component
public class RepositoryService {
    private final GithubClient githubClient;

    RepositoryService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public Repository userRepository(String owner, String name) {
        var githubRepository = githubClient.fetchRepositoryInfo(owner, name);
        return new Repository(
                githubRepository.getName(),
                githubRepository.getOwner().getName(),
                githubRepository.getLanguage(),
                githubRepository.getForks(),
                githubRepository.getStars()
        );
    }
}
