package com.kukulam.githubspring.api.blackbox.dto;

import java.util.Objects;

public class TestingGithubRepositoryOwner {
    private String login;

    public TestingGithubRepositoryOwner(String login) {
        this.login = login;
    }

    public TestingGithubRepositoryOwner() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestingGithubRepositoryOwner that = (TestingGithubRepositoryOwner) o;
        return Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
