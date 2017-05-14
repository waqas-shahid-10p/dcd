package com.crossover.service.base;

import com.crossover.code.Analyzer;
import com.crossover.exception.AnalyzerException;
import com.crossover.model.CodeRepo;
import com.crossover.model.DeadCodeOccurrence;
import com.crossover.model.GitRepo;
import java.util.List;

public interface CodeAnalyzer extends Analyzer<GitRepo, CodeRepo> {

  List<DeadCodeOccurrence> getDeadCodeOccurrences(final GitRepo gitRepo) throws AnalyzerException;
}