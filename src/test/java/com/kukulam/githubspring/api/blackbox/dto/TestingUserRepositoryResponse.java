package com.kukulam.githubspring.api.blackbox.dto;

import java.util.Objects;

public class TestingUserRepositoryResponse {
    private String name;
    private String owner;
    private String language;
    private int forks;
    private int stars;

    public TestingUserRepositoryResponse() {
    }

    public TestingUserRepositoryResponse(String name, String owner, String language, int forks, int stars) {
        this.name = name;
        this.owner = owner;
        this.language = language;
        this.forks = forks;
        this.stars = stars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestingUserRepositoryResponse that = (TestingUserRepositoryResponse) o;
        return forks == that.forks &&
                stars == that.stars &&
                name.equals(that.name) &&
                owner.equals(that.owner) &&
                language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner, language, forks, stars);
    }
}
