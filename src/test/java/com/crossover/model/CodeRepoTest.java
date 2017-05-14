package com.crossover.model;

import com.crossover.repository.base.CodeRepository;
import javax.validation.ConstraintViolationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class CodeRepoTest {

  @ClassRule
  public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();

  @Autowired
  private CodeRepository codeRepository;

  private String[] possibleBlankValues() {
    return new String[]{
        null, "", "   "
    };
  }

  @Test(expected = ConstraintViolationException.class)
  @Parameters(method = "possibleBlankValues")
  public void testUrlCannotBeBlank(final String url) {
    codeRepository.save(new CodeRepo(url));
  }

  @Test(expected = ConstraintViolationException.class)
  @Parameters({"", "https://github.com/jonschlinkert/", "/main.html", "www.example.com/main.html",
      "http:www.example.com/main.html"})
  public void testUrlCannotBeInvalid(final String url) {
    codeRepository.save(new CodeRepo(url));
  }

  @Test
  @Parameters({"http://github.com/jonschlinkert/is-git-url.git",
      "https://github.com/jonschlinkert/is-git-url.git"})
  public void testValidUrls(final String url) {
    codeRepository.save(new CodeRepo(url));
  }

  @Test(expected = ConstraintViolationException.class)
  public void testStatusShouldNotBeNull() {
    codeRepository
        .save(new CodeRepo("http://github.com/jonschlinkert/is-git-url.git").withStatus(null));
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void testUrlShouldBeUnique() {
    final String url = "http://github.com/jonschlinkert/is-git-url.git";
    codeRepository.save(new CodeRepo(url));
    codeRepository.save(new CodeRepo(url));
  }
}