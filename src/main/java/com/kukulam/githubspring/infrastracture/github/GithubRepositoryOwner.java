package com.kukulam.githubspring.infrastracture.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubRepositoryOwner {
    @JsonProperty("login")
    private String name;

    public GithubRepositoryOwner() {
    }

    public GithubRepositoryOwner(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
