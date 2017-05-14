package com.crossover.service;

import com.crossover.dto.RepoDTO;
import com.crossover.model.CodeRepo;
import com.crossover.model.DeadCodeOccurrence;
import com.crossover.repository.base.CodeRepository;
import com.crossover.service.base.CodeRepoService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.NotFoundException;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeRepoServiceTest {

  @Autowired
  private CodeRepoService codeRepoService;
  @Autowired
  private CodeRepository codeRepository;

  @After
  public void tearDown() {
    codeRepository.deleteAll();
  }

  @Test(expected = NotFoundException.class)
  public void testGetShouldThrowExceptionWhenCodeRepoIsNotFound() {
    codeRepoService.get(0);
  }

  @Test(expected = NotFoundException.class)
  public void testAnalyzeShouldThrowExceptionWhenCodeRepoIsNotFound() {
    codeRepoService.analyze(0);
  }

  @Test
  public void testAnalyzeShouldSendTheRequestToAnalyzeTheRepo() {
    final CodeRepo codeRepo = getCodeRepoInstance("http://github.com/abc.git");
    codeRepository.save(codeRepo);
    codeRepoService.analyze(codeRepo.getId());
  }

  @Test
  public void testGetShouldReturnObjectWhenIdIsValid() {
    final CodeRepo codeRepo = codeRepository.save(new CodeRepo("http://github.com/abc.git"));
    final CodeRepo testCodeRepo = codeRepoService.get(codeRepo.getId());
    assert testCodeRepo != null;
    assert testCodeRepo.getId() == codeRepo.getId();
    assert testCodeRepo.getUrl().equals(codeRepo.getUrl());
    assert testCodeRepo.getStatus() == codeRepo.getStatus();
  }

  @Test(expected = NotFoundException.class)
  public void testGetDeadCodeOccurrencesShouldThrowExceptionWhenCodeRepoIsNotFound() {
    codeRepoService.getDeadCodeOccurrences(0, 0, 10);
  }

  @Test
  public void testGetDeadCodeOccurrencesShouldReturnListWhenIdIsValid() {
    final CodeRepo codeRepo = getCodeRepoInstance("http://github.com/getDeadCodeOccurrences.git");
    codeRepository.save(codeRepo);
    final Page<DeadCodeOccurrence> deadCodeOccurrences = codeRepoService
        .getDeadCodeOccurrences(codeRepo.getId(), 0, 10);
    assert deadCodeOccurrences.getContent().size() == codeRepo.getDeadCodeOccurrences().size();
    for (final DeadCodeOccurrence occurrence : deadCodeOccurrences) {
      occurrence.getRepo().equals(codeRepo);
    }
  }

  @Test
  public void testListShouldReturnEmptyListWhenNothingIsinDB() {
    assert codeRepoService.list(0, 10).getContent().size() == 0;
  }

  @Test
  public void testListShouldReturnReposWhenPageParametersAreValid() {
    codeRepository.save(getCodeRepoInstance("http://github.com/getDeadCodeOccurrences.git"));
    assert codeRepoService.list(0, 5).getContent().size() == 1;
  }

  @Test
  public void testListShouldReturnPageObjectWithCorrectPaginationParamters() {
    List<CodeRepo> repos = new ArrayList<>(25);
    for (int i = 0; i < 25; i++) {
      repos.add(getCodeRepoInstance("http://github.com/getDeadCodeOccurrences" + i + ".git"));
    }
    codeRepository.save(repos);
    for (int i = 0; i < 5; i++) {
      assert codeRepoService.list(i, 5).getContent().size() == 5;
    }
  }

  private CodeRepo getCodeRepoInstance(final String url) {
    final CodeRepo codeRepo = new CodeRepo(url);
    codeRepo.setDeadCodeOccurrences(new ArrayList<DeadCodeOccurrence>(5) {{
      add(new DeadCodeOccurrence().withRepo(codeRepo).withFile("abc.java")
          .withKind("Private Static Variable").withLine("5").withValue("a1"));
      add(new DeadCodeOccurrence().withRepo(codeRepo).withFile("def.java").withKind("Class")
          .withLine("1").withValue("Custom"));
      add(new DeadCodeOccurrence().withRepo(codeRepo).withFile("ghi.java")
          .withKind("Private Method").withLine("31-45").withValue("a1b"));
      add(new DeadCodeOccurrence().withRepo(codeRepo).withFile("jkl.java").withKind("Parameter")
          .withLine("69").withValue("b1"));
      add(new DeadCodeOccurrence().withRepo(codeRepo).withFile("mno.java").withKind("TypeVariable")
          .withLine("42").withValue("A"));
    }});
    return codeRepo;
  }

  @Test(expected = IllegalArgumentException.class)
  @Ignore
  public void testCreateShouldThrowExceptionWhenUrlIsNull() {
    codeRepoService.create(null);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testCreateShouldThrowExceptionWhenUrlIsBlank() {
    codeRepoService.create(new RepoDTO());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void testCreateShouldThrowExceptionWhenCodeRepoAlreadyExists() {
    final CodeRepo codeRepo = getCodeRepoInstance("http://github.com/create.git");
    codeRepository.save(codeRepo);
    codeRepoService.create(new RepoDTO().withUrl(codeRepo.getUrl()));
  }

  @Test
  public void testCreateShouldSuccessfullyCreateTheCodeRepo() {
    assert
        codeRepoService.create(new RepoDTO().withUrl("http://github.com/create.git")).getId()
            == codeRepository.findAll()
            .iterator().next().getId();
  }
}