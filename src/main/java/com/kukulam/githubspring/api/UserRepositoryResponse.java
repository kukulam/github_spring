package com.kukulam.githubspring.api;

class UserRepositoryResponse {
    private String name;
    private String owner;
    private String language;
    private int forks;
    private int stars;

    public UserRepositoryResponse() {
    }

    public UserRepositoryResponse(String name, String owner, String language, int forks, int stars) {
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
}
