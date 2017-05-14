package com.crossover.model;

import com.crossover.exception.git.GitRepoException;

public interface GitRepo {

  String getDirectory();

  CodeRepo getCodeRepo();

  void pull() throws GitRepoException;
}
