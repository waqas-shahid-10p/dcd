package com.crossover.repository;

import com.crossover.exception.git.CloneException;
import com.crossover.exception.git.GitRepoException;
import com.crossover.model.CodeRepo;
import com.crossover.repository.base.GitRepository;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitRepositoryTest {

  @Autowired
  private GitRepository gitRepository;

  @Test
  public void testNoRepoIsReturnedWhenFolderWithNameDoesnotExist() throws IOException {
    assert gitRepository.get(new CodeRepo("corrupt url")) == null;
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  @Ignore
  public void testGetShouldThrowExceptionWhenCodeRepoIsNotProvided() throws IOException {
    gitRepository.get(null);
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  @Ignore
  public void testGetOrCloneShouldThrowExceptionWhenCodeRepoIsNotProvided()
      throws IOException, GitRepoException {
    gitRepository.getOrClone(null);
  }

  @Test(expected = CloneException.class)
  public void testGetOrCloneShouldThrowCloneExceptionWhenGitUrlIsInValid()
      throws IOException, GitRepoException {
    gitRepository.getOrClone(new CodeRepo("corrupt url"));
  }

  @Test(expected = CloneException.class)
  public void testGetOrCloneShouldThrowCloneExceptionWhenGitUrlIsValidButRepoDoesNotExist()
      throws IOException, GitRepoException {
    gitRepository.getOrClone(new CodeRepo("https://github.com/ThisRepoDoesnotExist.git"));
  }
}
