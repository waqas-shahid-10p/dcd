package com.crossover.repository.impl;

import com.crossover.exception.git.CloneException;
import com.crossover.exception.git.GitRepoException;
import com.crossover.model.CodeRepo;
import com.crossover.model.GitRepo;
import com.crossover.repository.base.GitRepository;
import java.io.File;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;

@org.springframework.stereotype.Repository
public class GitRepositoryImpl implements GitRepository {

  @Value("${REPO_FOLDER:/}")
  private String baseRepoFolder;

  @Override
  public GitRepo get(@Nonnull final CodeRepo codeRepo) throws IOException {
    File file = getRepoDirectory(codeRepo);
    if (file.exists()) {
      return new GitRepoImpl(file.getAbsolutePath(), codeRepo, Git.open(file));
    }
    return null;
  }

  @Override
  public GitRepo getOrClone(@Nonnull final CodeRepo codeRepo) throws IOException, CloneException {
    GitRepo gitRepo = get(codeRepo);
    if (gitRepo == null) {
      final File dir = getRepoDirectory(codeRepo);
      try {
        Git.cloneRepository()
            .setURI(codeRepo.getUrl())
            .setDirectory(dir)
            .call();
        gitRepo = get(codeRepo);
      } catch (GitAPIException e) {
        FileUtils.forceDelete(dir);
        throw new CloneException(e);
      }
    }
    return gitRepo;
  }

  private File getRepoDirectory(@Nonnull final CodeRepo codeRepo) {
    return new File(baseRepoFolder + "/" + codeRepo.getId());
  }

  private static class GitRepoImpl implements GitRepo {

    private final String directoryPath;
    private final CodeRepo codeRepo;
    private final Git repository;

    GitRepoImpl(final String directoryPath, final CodeRepo codeRepo, final Git repository) {
      this.directoryPath = directoryPath;
      this.codeRepo = codeRepo;
      this.repository = repository;
    }

    @Override
    public String getDirectory() {
      return directoryPath;
    }

    @Override
    public CodeRepo getCodeRepo() {
      return codeRepo;
    }

    @Override
    public void pull() throws GitRepoException {
      try {
        repository.pull().call();
      } catch (GitAPIException ex) {
        throw new GitRepoException(ex);
      }
    }
  }
}