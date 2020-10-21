package com.kukulam.githubspring.api.blackbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.kukulam.githubspring.api.IntegrationTest;
import com.kukulam.githubspring.api.blackbox.dto.TestingGithubRepositoryOwner;
import com.kukulam.githubspring.api.blackbox.dto.TestingGithubRepositoryResponse;
import com.kukulam.githubspring.api.blackbox.dto.TestingUserRepositoryResponse;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class BlackboxRepositoryEndpointTest extends IntegrationTest {

    private static final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8099));
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setup() {
        wireMockServer.start();
    }

    @AfterEach
    void cleanup() {
        wireMockServer.resetAll();
    }

    @Test
    public void shouldFetchUserRepository() throws JsonProcessingException {
        // given
        var owner = "kukulam";
        var repositoryName = "testowe_repo1";
        var url = baseUrl() + "/repository/" + owner + "?name=" + repositoryName;
        var mockedRepository = new TestingGithubRepositoryResponse(
                repositoryName,
                new TestingGithubRepositoryOwner(owner),
                "Java",
                0,
                1
        );

        var expectedResponseBody = new TestingUserRepositoryResponse(
                mockedRepository.getName(),
                mockedRepository.getOwner().getLogin(),
                mockedRepository.getLanguage(),
                mockedRepository.getForks(),
                mockedRepository.getStargazers_count()
        );

        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.asString(), APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(mockedRepository))
                )
        );

        // when
        var response = restTemplate.getForEntity(url, TestingUserRepositoryResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponseBody);
    }

    @Test
    public void shouldNotFoundUseRepository() {
        // given
        var owner = "kukulam";
        var repositoryName = "testowe_repo2";
        var url = baseUrl() + "/repository/" + owner + "?name=" + repositoryName;

        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName))
                .willReturn(aResponse()
                        .withStatus(404)
                )
        );

        // when && then
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(url, TestingUserRepositoryResponse.class));
    }

    @Test
    public void shouldFetchTheSameRepositoryTwiceEvenWhenItsChangedCauseOfCache() throws JsonProcessingException {
        // given
        var owner = "kukulam";
        var repositoryName = "testowe_repo3";
        var url = baseUrl() + "/repository/" + owner + "?name=" + repositoryName;
        var mockedRepository = new TestingGithubRepositoryResponse(
                repositoryName,
                new TestingGithubRepositoryOwner(owner),
                "Java",
                0,
                1
        );
        var mockedChangedRepository = new TestingGithubRepositoryResponse(
                repositoryName,
                new TestingGithubRepositoryOwner(owner),
                "Scala",
                99,
                100
        );

        var expectedResponseBody = new TestingUserRepositoryResponse(
                mockedRepository.getName(),
                mockedRepository.getOwner().getLogin(),
                mockedRepository.getLanguage(),
                mockedRepository.getForks(),
                mockedRepository.getStargazers_count()
        );

        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName))
                .inScenario("Repo")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.asString(), APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(mockedRepository))
                )
                .willSetStateTo("After first stub")
        );
        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName))
                .inScenario("Repo")
                .whenScenarioStateIs("After first stub")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.asString(), APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(mockedChangedRepository))
                )
        );

        // when
        var responseRepository1 = restTemplate.getForEntity(url, TestingUserRepositoryResponse.class);
        var responseRepository2 = restTemplate.getForEntity(url, TestingUserRepositoryResponse.class);

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
        var repositoryName = "testowe_repo4";
        var url = baseUrl() + "/repository/" + owner + "?name=" + repositoryName;

        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName))
                .willReturn(aResponse()
                        .withStatus(500)
                )
        );
        // when && then
        assertThrows(HttpServerErrorException.InternalServerError.class, () -> restTemplate.getForEntity(url, TestingUserRepositoryResponse.class));
    }

    @Test
    public void shouldFetchListOfUserRepositories() throws JsonProcessingException {
        // given
        var owner = "kukulam";
        var repositoryName1 = "testowe_repo5";
        var repositoryName2 = "testowe_repo6";
        var url = baseUrl() + "/repositories/" + owner + "?names=" + repositoryName1 + "," + repositoryName2;
        var mockedRepository1 = new TestingGithubRepositoryResponse(
                repositoryName1,
                new TestingGithubRepositoryOwner(owner),
                "Java",
                0,
                1
        );
        var mockedRepository2 = new TestingGithubRepositoryResponse(
                repositoryName1,
                new TestingGithubRepositoryOwner(owner),
                "Java",
                0,
                2
        );

        var expectedRepository1 = new TestingUserRepositoryResponse(
                mockedRepository1.getName(),
                mockedRepository1.getOwner().getLogin(),
                mockedRepository1.getLanguage(),
                mockedRepository1.getForks(),
                mockedRepository1.getStargazers_count()
        );

        var expectedRepository2 = new TestingUserRepositoryResponse(
                mockedRepository2.getName(),
                mockedRepository2.getOwner().getLogin(),
                mockedRepository2.getLanguage(),
                mockedRepository2.getForks(),
                mockedRepository2.getStargazers_count()
        );

        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.asString(), APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(mockedRepository1))
                )
        );
        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName2))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.asString(), APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(mockedRepository2))
                )
        );

        // when
        var response = restTemplate.getForEntity(url, TestingUserRepositoryResponse[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyInAnyOrder(expectedRepository1, expectedRepository2);
    }

    @Test
    public void shouldReturnNotFoundCodeWhenOneOfRepositoryDoesNotExist() throws JsonProcessingException {
        // given
        var owner = "kukulam";
        var repositoryName1 = "testowe_repo7";
        var repositoryName2 = "testowe_repo8";
        var url = baseUrl() + "/repositories/" + owner + "?names=" + repositoryName1 + "," + repositoryName2;
        var mockedRepository1 = new TestingGithubRepositoryResponse(
                repositoryName1,
                new TestingGithubRepositoryOwner(owner),
                "Java",
                0,
                1
        );

        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.asString(), APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(mockedRepository1))
                )
        );
        wireMockServer.stubFor(get(urlEqualTo("/repos/" + owner + "/" + repositoryName2))
                .willReturn(aResponse()
                        .withStatus(404)
                )
        );

        // when && then
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(url, TestingUserRepositoryResponse.class));
    }
}

