package com.crossover.service.base;

import com.crossover.dto.RepoDTO;
import com.crossover.model.CodeRepo;
import com.crossover.model.DeadCodeOccurrence;
import org.springframework.data.domain.Page;

public interface CodeRepoService {

  Page<CodeRepo> list(int page, int size);

  CodeRepo get(long id);

  Page<DeadCodeOccurrence> getDeadCodeOccurrences(long id, int page, int size);

  CodeRepo create(RepoDTO repoDTO);

  CodeRepo changeStatus(long id, CodeRepo.Status status);

  CodeRepo update(final CodeRepo codeRepo);

  void analyze(long id);
}
