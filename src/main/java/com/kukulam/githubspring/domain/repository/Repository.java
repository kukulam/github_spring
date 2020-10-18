package com.kukulam.githubspring.domain.repository;

public class Repository {
    private final String name;
    private final String owner;
    private final String language;
    private final int forks;
    private final int stars;

    public Repository(String name, String owner, String language, int forks, int stars) {
        this.name = name;
        this.owner = owner;
        this.language = language;
        this.forks = forks;
        this.stars = stars;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getLanguage() {
        return language;
    }

    public int getForks() {
        return forks;
    }

    public int getStars() {
        return stars;
    }
}
