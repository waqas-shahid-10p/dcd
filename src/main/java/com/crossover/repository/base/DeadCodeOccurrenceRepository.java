package com.crossover.repository.base;

import com.crossover.model.CodeRepo;
import com.crossover.model.DeadCodeOccurrence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeadCodeOccurrenceRepository extends
    PagingAndSortingRepository<DeadCodeOccurrence, Long> {

  Page<DeadCodeOccurrence> findAllByRepo(CodeRepo repo, Pageable pageable);
}