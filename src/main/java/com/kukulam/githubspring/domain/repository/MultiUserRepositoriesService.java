package com.kukulam.githubspring.domain.repository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class MultiUserRepositoriesService {
    private final RepositoryService cachedUserRepositoryService;
    private final ExecutorService executorService;

    public MultiUserRepositoriesService(RepositoryService cachedUserRepositoryService) {
        this.cachedUserRepositoryService = cachedUserRepositoryService;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public List<Repository> userRepository(String owner, List<String> names) {
        var tasks = names.stream()
            .map(name -> {
                Supplier<Repository> task = () -> cachedUserRepositoryService.userRepository(owner, name);
                return task;
            }).collect(Collectors.toList());

        var tasksResult = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, executorService))
                .collect(Collectors.toList());

        CompletableFuture.allOf(tasksResult.toArray(CompletableFuture[]::new))
                .orTimeout(5, TimeUnit.SECONDS).join();

        return tasksResult.stream()
                .map(taskResult -> {
                    try {
                        return taskResult.get(100, TimeUnit.MILLISECONDS);
                    } catch (Exception e){
                        throw new RuntimeException("Problem with fetching user repository in time.");
                    }
                })
                .collect(Collectors.toList());
    }
}
