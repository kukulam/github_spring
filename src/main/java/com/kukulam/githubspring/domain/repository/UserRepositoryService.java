package com.kukulam.githubspring.domain.repository;

import com.kukulam.githubspring.infrastracture.github.GithubClient;
import org.springframework.stereotype.Component;

@Component
class UserRepositoryService implements RepositoryService {
    private final GithubClient githubClient;

    UserRepositoryService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    @Override
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

