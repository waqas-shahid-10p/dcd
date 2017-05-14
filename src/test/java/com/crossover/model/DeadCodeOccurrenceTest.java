package com.crossover.model;

import com.crossover.repository.base.CodeRepository;
import java.util.ArrayList;
import javax.validation.ConstraintViolationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class DeadCodeOccurrenceTest {

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
  public void testFileCannotBeBlank(final String file) {
    final CodeRepo codeRepo = new CodeRepo("http://github.com/abc.git");
    codeRepo.setDeadCodeOccurrences(new ArrayList<DeadCodeOccurrence>() {{
      add(new DeadCodeOccurrence().withFile(file).withKind("Method").withLine("45").withValue("abc")
          .withRepo(codeRepo));
    }});
    codeRepository.save(codeRepo
    );
  }

  @Test(expected = ConstraintViolationException.class)
  @Parameters(method = "possibleBlankValues")
  public void testKindCannotBeBlank(final String kind) {
    final CodeRepo codeRepo = new CodeRepo("http://github.com/abc.git");
    codeRepo.setDeadCodeOccurrences(new ArrayList<DeadCodeOccurrence>() {{
      add(new DeadCodeOccurrence().withFile("file.java").withKind(kind).withLine("45")
          .withValue("abc").withRepo(codeRepo));
    }});
    codeRepository.save(codeRepo
    );
  }

  @Test(expected = ConstraintViolationException.class)
  @Parameters(method = "possibleBlankValues")
  public void testLineCannotBeBlank(final String line) {
    final CodeRepo codeRepo = new CodeRepo("http://github.com/abc.git");
    codeRepo.setDeadCodeOccurrences(new ArrayList<DeadCodeOccurrence>() {{
      add(new DeadCodeOccurrence().withFile("file.java").withKind("Method").withLine(line)
          .withValue("abc").withRepo(codeRepo));
    }});
    codeRepository.save(codeRepo
    );
  }

  @Test(expected = ConstraintViolationException.class)
  @Parameters(method = "possibleBlankValues")
  public void testValueCannotBeBlank(final String value) {
    final CodeRepo codeRepo = new CodeRepo("http://github.com/abc.git");
    codeRepo.setDeadCodeOccurrences(new ArrayList<DeadCodeOccurrence>() {{
      add(new DeadCodeOccurrence().withFile("file.java").withKind("Method").withLine("45")
          .withValue(value).withRepo(codeRepo));
    }});
    codeRepository.save(codeRepo
    );
  }
}