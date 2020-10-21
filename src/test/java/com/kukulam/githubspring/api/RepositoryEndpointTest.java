package com.kukulam.githubspring.api;

import com.kukulam.githubspring.infrastracture.github.GithubClient;
import com.kukulam.githubspring.infrastracture.github.GithubRepository;
import com.kukulam.githubspring.infrastracture.github.GithubRepositoryOwner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/* Example of not the best way of integration tests */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RepositoryEndpointTest extends IntegrationTest {

    @MockBean
    private GithubClient mockedGithubClient;

    @Test
    public void shouldFetchUserRepository() {
        // given
        var owner = "kukulam";
        var repositoryName = "testowe_repo";
        var url = baseUrl() + "/repository/" + owner + "?name=" + repositoryName;
        var mockedRepository = new GithubRepository(
                repositoryName,
                new GithubRepositoryOwner(owner),
                "Java",
                0,
                1
        );
        var expectedResponseBody = new UserRepositoryResponse(
                mockedRepository.getName(),
                mockedRepository.getOwner().getName(),
                mockedRepository.getLanguage(),
                mockedRepository.getForks(),
                mockedRepository.getStars()
        );

        when(mockedGithubClient.fetchRepositoryInfo(owner, repositoryName))
                .thenReturn(Optional.of(mockedRepository));

        // when
        var response = restTemplate.getForEntity(url, UserRepositoryResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponseBody);
    }

    @Test
    public void shouldNotFoundUseRepository() {
        // given
        var owner = "kukulam";
        var repositoryName = "testowe_repo";
        var url = baseUrl() + "/repository/" + owner + "?name=" + repositoryName;

        when(mockedGithubClient.fetchRepositoryInfo(owner, repositoryName))
                .thenReturn(Optional.empty());

        // when && then
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(url, UserRepositoryResponse.class));
    }

    @Test
    public void shouldFetchTheSameRepositoryTwiceEvenWhenItsChangedCauseOfCache() {
        // given
        var owner = "kukulam";
        var repositoryName = "testowe_repo";
        var url = baseUrl() + "/repository/" + owner + "?name=" + repositoryName;
        var mockedRepository = new GithubRepository(
                repositoryName,
                new GithubRepositoryOwner(owner),
                "Java",
                0,
                1
        );
        var mockedChangedRepository = new GithubRepository(
                repositoryName,
                new GithubRepositoryOwner(owner),
                "Scala",
                99,
                100
        );

        var expectedResponseBody = new UserRepositoryResponse(
                mockedRepository.getName(),
                mockedRepository.getOwner().getName(),
                mockedRepository.getLanguage(),
                mockedRepository.getForks(),
                mockedRepository.getStars()
        );

        when(mockedGithubClient.fetchRepositoryInfo(owner, repositoryName))
                .thenReturn(Optional.of(mockedRepository))
                .thenReturn(Optional.of(mockedChangedRepository));

        // when
        var responseRepository1 = restTemplate.getForEntity(url, UserRepositoryResponse.class);
        var responseRepository2 = restTemplate.getForEntity(url, UserRepositoryResponse.class);

        // then
        assertThat(responseRepository1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseRepository1.getBody()).isEqualTo(expectedResponseBody);

        assertThat(responseRepository2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseRepository2.getBody()).isEqualTo(expectedResponseBody);
    }

    @Test
    void shouldReturnInternalErrorWhenThereIsProblemWithProcessingRepository() {
        // given
        var owner = "kukulam";
        var repositoryName = "testowe_repo";
        var url = baseUrl() + "/repository/" + owner + "?name=" + repositoryName;

        when(mockedGithubClient.fetchRepositoryInfo(owner, repositoryName))
                .thenThrow(new RuntimeException("Problem with processing"));

        // when && then
        assertThrows(HttpServerErrorException.InternalServerError.class, () -> restTemplate.getForEntity(url, UserRepositoryResponse.class));
    }

    @Test
    public void shouldFetchListOfUserRepositories() {
        // given
        var owner = "kukulam";
        var repositoryName1 = "testowe_repo1";
        var repositoryName2 = "testowe_repo2";
        var url = baseUrl() + "/repositories/" + owner + "?names=" + repositoryName1 + "," + repositoryName2;
        var mockedRepository1 = new GithubRepository(
                repositoryName1,
                new GithubRepositoryOwner(owner),
                "Java",
                0,
                1
        );
        var mockedRepository2 = new GithubRepository(
                repositoryName1,
                new GithubRepositoryOwner(owner),
                "Java",
                0,
                2
        );

        var expectedRepository1 = new UserRepositoryResponse(
                mockedRepository1.getName(),
                mockedRepository1.getOwner().getName(),
                mockedRepository1.getLanguage(),
                mockedRepository1.getForks(),
                mockedRepository1.getStars()
        );

        var expectedRepository2 = new UserRepositoryResponse(
                mockedRepository2.getName(),
                mockedRepository2.getOwner().getName(),
                mockedRepository2.getLanguage(),
                mockedRepository2.getForks(),
                mockedRepository2.getStars()
        );

        when(mockedGithubClient.fetchRepositoryInfo(owner, repositoryName1))
                .thenReturn(Optional.of(mockedRepository1));
        when(mockedGithubClient.fetchRepositoryInfo(owner, repositoryName2))
                .thenReturn(Optional.of(mockedRepository2));

        // when
        var response = restTemplate.getForEntity(url, UserRepositoryResponse[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyInAnyOrder(expectedRepository1, expectedRepository2);
    }

    @Test
    public void shouldReturnNotFoundCodeWhenOneOfRepositoryDoesNotExist() {
        // given
        var owner = "kukulam";
        var repositoryName1 = "testowe_repo1";
        var repositoryName2 = "testowe_repo2";
        var url = baseUrl() + "/repositories/" + owner + "?names=" + repositoryName1 + "," + repositoryName2;
        var mockedRepository1 = new GithubRepository(
                repositoryName1,
                new GithubRepositoryOwner(owner),
                "Java",
                0,
                1
        );

        when(mockedGithubClient.fetchRepositoryInfo(owner, repositoryName1))
                .thenReturn(Optional.of(mockedRepository1));
        when(mockedGithubClient.fetchRepositoryInfo(owner, repositoryName2))
                .thenReturn(Optional.empty());

        // when && then
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(url, UserRepositoryResponse.class));
    }
}