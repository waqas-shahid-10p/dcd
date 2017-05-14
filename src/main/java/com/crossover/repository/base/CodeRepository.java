package com.crossover.repository.base;

import com.crossover.model.CodeRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CodeRepository extends PagingAndSortingRepository<CodeRepo, Long> {

  int countByUrlEquals(String url);

  @Query("from CodeRepo c left join fetch c.deadCodeOccurrences where c.id = :id")
  CodeRepo getCodeRepoById(@Param("id") final long id);
}
