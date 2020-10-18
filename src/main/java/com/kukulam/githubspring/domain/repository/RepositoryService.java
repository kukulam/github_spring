package com.kukulam.githubspring.domain.repository;

public interface RepositoryService {
    Repository userRepository(String owner, String name);
}
