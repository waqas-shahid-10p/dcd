package com.crossover.repository.base;

import com.crossover.exception.git.CloneException;
import com.crossover.model.CodeRepo;
import com.crossover.model.GitRepo;
import java.io.IOException;
import javax.annotation.Nonnull;

public interface GitRepository {

  GitRepo get(@Nonnull CodeRepo codeRepo) throws IOException;

  GitRepo getOrClone(@Nonnull CodeRepo codeRepo) throws IOException, CloneException;
}
