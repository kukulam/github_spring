package com.kukulam.githubspring.api;

import com.kukulam.githubspring.domain.repository.RepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/repositories")
public class RepositoryEndpoint {

    private final RepositoryService repositoryService;

    RepositoryEndpoint(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping
    @RequestMapping("/{owner}")
    ResponseEntity<UserRepositoryResponse> userRepository(@PathVariable String owner, @RequestParam String name) {
        var fetchedRepository = repositoryService.userRepository(owner, name);

        var userRepository = new UserRepositoryResponse(
                fetchedRepository.getName(),
                fetchedRepository.getOwner(),
                fetchedRepository.getLanguage(),
                fetchedRepository.getForks(),
                fetchedRepository.getStars()
        );

        return ResponseEntity.of(Optional.of(userRepository));
    }
}

