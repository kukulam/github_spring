package com.kukulam.githubspring.infrastracture.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubRepositoryOwner that = (GithubRepositoryOwner) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
