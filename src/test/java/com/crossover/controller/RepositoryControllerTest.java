package com.crossover.controller;

import com.crossover.model.CodeRepo;
import java.util.List;
import javax.ws.rs.core.HttpHeaders;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepositoryControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testAListShouldReturnAnEmptyListWhenNoCodeRepoIsInDB() {
    final ResponseEntity<List> response = restTemplate
        .getForEntity("/api/repositories?page=0&size=10", List.class);
    assert response.getStatusCode().is2xxSuccessful();
    assert response.hasBody();
    assert response.getBody().size() == 0;
  }

  @Test
  public void testListShouldReturn400WhenPageAndSizeAreNotProvided() {
    assert restTemplate
        .getForEntity("/api/repositories?page=0", String.class).getStatusCode().is4xxClientError();
  }

  @Test
  public void testListShouldReturnListWhenThereAreCodeRepoIsInDB() {
    for (int i = 0; i < 5; i++) {
      createCodeRepo(
          "http://github.com/testListShouldReturnListWhenThereAreCodeRepoIsInDB" + i + ".git");
    }
    final ResponseEntity<List> response = restTemplate
        .getForEntity("/api/repositories?page=0&size=10", List.class);
    assert response.getStatusCode().is2xxSuccessful();
    assert response.hasBody();
    assert response.getBody().size() > 0;
  }

  @Test
  public void testListShouldReturnPaginationLinkInHeader() {
    for (int i = 0; i < 5; i++) {
      createCodeRepo("http://github.com/testListShouldReturnPaginationLinkInHeader" + i + ".git");
    }
    final ResponseEntity<List> response = restTemplate
        .getForEntity("/api/repositories?page=0&size=10", List.class);
    assert response.getStatusCode().is2xxSuccessful();
    assert response.hasBody();
    assert response.getBody().size() > 0;
    assert !"".equals(response.getHeaders().get(HttpHeaders.LINK));
  }

  @Test
  public void testGetShouldThrow400WhenCodeRepoIsNotFound() {
    assert restTemplate
        .getForEntity("/api/repositories/0", String.class).getStatusCode().is4xxClientError();
  }

  @Test
  public void testGetShouldReturnTheObjectWhenIdIsValid() {
    final CodeRepo codeRepo = createCodeRepo(
        "http://github.com/testGetShouldReturnTheObjectWhenIdIsValid.git");
    assert restTemplate
        .getForEntity("/api/repositories/" + codeRepo.getId(), CodeRepo.class).getBody().getUrl()
        .equals(codeRepo.getUrl());
  }

  @Test
  @Ignore
  public void testCreateShouldReturn400WhenObjectIsInvalid() {
    assert restTemplate
        .postForEntity("/api/repositories/", null, String.class).getStatusCode().is4xxClientError();
  }

  @Test
  public void testCreateShouldReturn400WhenUrlIsInvalid() {
    assert restTemplate
        .postForEntity("/api/repositories/", new CodeRepo(""), String.class).getStatusCode()
        .is4xxClientError();
  }

  @Test
  public void testCreateShouldReturn400WhenUrlIsEmpty() {
    assert restTemplate
        .postForEntity("/api/repositories/", new CodeRepo(""), String.class).getStatusCode()
        .is4xxClientError();
  }

  @Test
  public void testGetDeadCodeOccurencesReturnEmptyListWhenNoOccurrencesAreRecordedYet() {
    final CodeRepo codeRepo = createCodeRepo(
        "http://github.com/testGetDeadCodeOccurencesReturnEmptyListWhenNoOccurrencesAreRecordedYet.git");
    final ResponseEntity<List> responseEntity = restTemplate
        .getForEntity("/api/repositories/" + codeRepo.getId() + "/analysis/deadcode?page=0&size=10",
            List.class);
    assert responseEntity.getStatusCode().is2xxSuccessful();
    assert responseEntity.hasBody();
    assert responseEntity.getBody().size() == 0;
  }

  private CodeRepo createCodeRepo(final String url) {
    final ResponseEntity<CodeRepo> responseEntity = restTemplate
        .postForEntity("/api/repositories", new CodeRepo(url), CodeRepo.class);
    assert responseEntity.getStatusCode().is2xxSuccessful();
    return responseEntity.getBody();
  }
}
