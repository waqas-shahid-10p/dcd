package com.crossover.event;

import com.crossover.model.CodeRepo;
import org.springframework.context.ApplicationEvent;

public class CodeRepoEvent extends ApplicationEvent {

  public CodeRepoEvent(final CodeRepo codeRepo) {
    super(codeRepo);
  }
}
