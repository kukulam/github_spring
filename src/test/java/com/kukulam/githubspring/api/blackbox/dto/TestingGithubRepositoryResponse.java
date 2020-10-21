package com.kukulam.githubspring.api.blackbox.dto;

import java.util.Objects;

public class TestingGithubRepositoryResponse {
    private String name;
    private TestingGithubRepositoryOwner owner;
    private String language;
    private int stargazers_count;
    private int forks;

    public TestingGithubRepositoryResponse() {
    }

    public TestingGithubRepositoryResponse(String name, TestingGithubRepositoryOwner owner, String language, int stargazers_count, int forks) {
        this.name = name;
        this.owner = owner;
        this.language = language;
        this.stargazers_count = stargazers_count;
        this.forks = forks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestingGithubRepositoryOwner getOwner() {
        return owner;
    }

    public void setOwner(TestingGithubRepositoryOwner owner) {
        this.owner = owner;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestingGithubRepositoryResponse that = (TestingGithubRepositoryResponse) o;
        return stargazers_count == that.stargazers_count &&
                forks == that.forks &&
                Objects.equals(name, that.name) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner, language, stargazers_count, forks);
    }
}
