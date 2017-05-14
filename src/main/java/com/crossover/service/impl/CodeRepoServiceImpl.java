package com.crossover.service.impl;

import com.crossover.dto.RepoDTO;
import com.crossover.event.CodeRepoEvent;
import com.crossover.model.CodeRepo;
import com.crossover.model.DeadCodeOccurrence;
import com.crossover.repository.base.CodeRepository;
import com.crossover.repository.base.DeadCodeOccurrenceRepository;
import com.crossover.service.base.CodeRepoService;
import java.util.Date;
import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CodeRepoServiceImpl implements CodeRepoService {

  @Autowired
  private CodeRepository codeRepository;
  @Autowired
  private DeadCodeOccurrenceRepository deadCodeOccurrenceRepository;
  @Autowired
  private MessageSource messageSource;
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Override
  public CodeRepo get(final long id) {
    final CodeRepo repo = codeRepository.findOne(id);
    validateCodeRepo(id, repo);
    return repo;
  }

  private void validateCodeRepo(long id, CodeRepo repo) {
    if (repo == null) {
      throw new NotFoundException(
          messageSource.getMessage("object.not.found", new Object[]{"repo", id},
              LocaleContextHolder.getLocale()));
    }
  }

  @Override
  public Page<DeadCodeOccurrence> getDeadCodeOccurrences(final long id, int page, int size) {
    return deadCodeOccurrenceRepository.findAllByRepo(get(id), new PageRequest(page, size));
  }

  @Override
  public Page<CodeRepo> list(final int page, final int size) {
    final Pageable pageable = new PageRequest(page, size);
    return codeRepository.findAll(pageable);
  }

  @Override
  public void analyze(final long id) {
    final CodeRepo codeRepo = get(id);
    eventPublisher.publishEvent(new CodeRepoEvent(codeRepo));
  }

  @Override
  @Transactional
  public CodeRepo create(@Nonnull final RepoDTO repoDTO) {
    if (codeRepository.countByUrlEquals(repoDTO.getUrl()) == 0) {
      final CodeRepo codeRepo = codeRepository.save(new CodeRepo(repoDTO.getUrl()));
      eventPublisher.publishEvent(new CodeRepoEvent(codeRepo));
      return codeRepo;
    }
    throw new DataIntegrityViolationException(
        messageSource.getMessage("repo.url.exists", new Object[]{repoDTO.getUrl()},
            LocaleContextHolder.getLocale()));
  }

  @Override
  public CodeRepo changeStatus(@Nonnull final long id, @Nonnull final CodeRepo.Status status) {
    final CodeRepo codeRepo = get(id).withStatus(status);
    update(codeRepo);
    return codeRepo;
  }

  @Override
  @Transactional
  public CodeRepo update(@Nonnull final CodeRepo codeRepo) {
    codeRepo.setUpdatedAt(new Date());
    codeRepository.save(codeRepo);
    return codeRepo;
  }
}
