package com.kukulam.githubspring.infrastracture.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GithubRepository {
    private String name;
    private GithubRepositoryOwner owner;
    private String language;
    @JsonProperty("stargazers_count")
    private int stars;
    private int forks;

    public GithubRepository() {
    }

    public GithubRepository(String name, GithubRepositoryOwner owner, String language, int stars, int forks) {
        this.name = name;
        this.owner = owner;
        this.language = language;
        this.stars = stars;
        this.forks = forks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GithubRepositoryOwner getOwner() {
        return owner;
    }

    public void setOwner(GithubRepositoryOwner owner) {
        this.owner = owner;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
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
        GithubRepository that = (GithubRepository) o;
        return stars == that.stars &&
                forks == that.forks &&
                Objects.equals(name, that.name) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner, language, stars, forks);
    }
}
