package com.kukulam.githubspring.api;

import com.kukulam.githubspring.domain.repository.MultiUserRepositoriesService;
import com.kukulam.githubspring.domain.repository.RepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RepositoryEndpoint {

    private final RepositoryService cachedUserRepositoryService;
    private final MultiUserRepositoriesService multiUserRepositoriesService;

    public RepositoryEndpoint(
            RepositoryService cachedUserRepositoryService,
            MultiUserRepositoriesService multiUserRepositoriesService
    ) {
        this.cachedUserRepositoryService = cachedUserRepositoryService;
        this.multiUserRepositoriesService = multiUserRepositoriesService;
    }

    @GetMapping
    @RequestMapping("/repositories/{owner}")
    ResponseEntity<UserRepositoryResponse> userRepository(@PathVariable String owner, @RequestParam String name) {
        var fetchedRepository = cachedUserRepositoryService.userRepository(owner, name);

        var userRepository = new UserRepositoryResponse(
                fetchedRepository.getName(),
                fetchedRepository.getOwner(),
                fetchedRepository.getLanguage(),
                fetchedRepository.getForks(),
                fetchedRepository.getStars()
        );

        return ResponseEntity.of(Optional.of(userRepository));
    }

    @GetMapping
    @RequestMapping("/repository/{owner}")
    ResponseEntity<List<UserRepositoryResponse>> userRepositories(@PathVariable String owner, @RequestParam List<String> names) {
        var userRepositories = multiUserRepositoriesService.userRepository(owner, names).stream()
                .map(fetchedRepository -> new UserRepositoryResponse(
                        fetchedRepository.getName(),
                        fetchedRepository.getOwner(),
                        fetchedRepository.getLanguage(),
                        fetchedRepository.getForks(),
                        fetchedRepository.getStars()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.of(Optional.of(userRepositories));
    }
}

