package com.kukulam.githubspring.domain.repository;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executors;

@Component
class CachedUserRepositoryService implements RepositoryService {

    private final RepositoryService userRepositoryService;
    private final LoadingCache<RepositoryCacheKey, Repository> cache;

    public CachedUserRepositoryService(RepositoryService userRepositoryService) {
        this.userRepositoryService = userRepositoryService;

        cache = Caffeine.newBuilder()
                .maximumSize(1_000)
                .executor(Executors.newFixedThreadPool(2))
                .expireAfterWrite(Duration.ofSeconds(5))
                .build(key -> this.userRepositoryService.userRepository(key.owner, key.name));
    }

    @Override
    public Repository userRepository(String owner, String name) {
        var key = new RepositoryCacheKey(owner, name);
        return cache.get(key);
    }

    static class RepositoryCacheKey {
        private final String owner;
        private final String name;

        public RepositoryCacheKey(String owner, String name) {
            this.owner = owner;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RepositoryCacheKey that = (RepositoryCacheKey) o;
            return owner.equals(that.owner) &&
                    name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(owner, name);
        }
    }
}

