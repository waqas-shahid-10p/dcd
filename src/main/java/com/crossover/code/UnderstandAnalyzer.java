package com.crossover.code;

import com.crossover.model.GitRepo;

public abstract class UnderstandAnalyzer<Return> implements Analyzer<GitRepo, Return> {

  private static final String DATABASE_PATH = "%s/project.udb";

  protected String getDatabasePath(final GitRepo gitRepo) {
    return String.format(DATABASE_PATH, gitRepo.getDirectory().replace("\\", "/"));
  }
}
