package com.crossover.service.impl;

import com.crossover.code.Analyzer;
import com.crossover.code.UnderstandAnalyzer;
import com.crossover.exception.AnalyzerException;
import com.crossover.model.CodeRepo;
import com.crossover.model.DeadCodeOccurrence;
import com.crossover.model.GitRepo;
import com.crossover.service.base.CodeAnalyzer;
import com.scitools.understand.Understand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("codeAnalyzer")
public class CodeAnalyzerImpl extends UnderstandAnalyzer<CodeRepo> implements CodeAnalyzer {

  private static final String COMMAND =
      "und create -languages java add %s analyze -all %s";

  static {
    Understand.loadNativeLibrary();
  }

  @Autowired()
  @Qualifier("deadCodeAnalyzer")
  private Analyzer<GitRepo, List<DeadCodeOccurrence>> deadCodeAnalyzer;

  @Override
  public CodeRepo analyze(final GitRepo gitRepo) throws AnalyzerException {
    final String databasePath = getDatabasePath(gitRepo);
    try {
      final Process undCommand = Runtime.getRuntime()
          .exec(String.format(COMMAND, gitRepo.getDirectory(), databasePath));
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(undCommand.getInputStream()));
      String line;

      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }

      reader = new BufferedReader(
          new InputStreamReader(undCommand.getErrorStream()));
      boolean hasError = false;
      final StringBuilder errorBuilder = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
        errorBuilder.append(line).append("\n");
        hasError = true;
      }
      if (hasError) {
        throw new AnalyzerException(errorBuilder.toString());
      }
    } catch (IOException ex) {
      throw new AnalyzerException(ex);
    }
    return gitRepo.getCodeRepo().withDeadCodeOccurrences(getDeadCodeOccurrences(gitRepo));
  }

  public List<DeadCodeOccurrence> getDeadCodeOccurrences(final GitRepo gitRepo)
      throws AnalyzerException {
    return deadCodeAnalyzer.analyze(gitRepo);
  }
}