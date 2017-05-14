package com.crossover.listener;

import com.crossover.event.CodeRepoEvent;
import com.crossover.event.base.AsyncListener;
import com.crossover.model.CodeRepo;
import com.crossover.model.CodeRepo.Status;
import com.crossover.model.GitRepo;
import com.crossover.repository.base.GitRepository;
import com.crossover.service.base.CodeAnalyzer;
import com.crossover.service.base.CodeRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AsyncListener
public class CodeRepoListener implements ApplicationListener<CodeRepoEvent> {

  @Autowired
  private CodeRepoService codeRepoService;
  @Autowired
  private GitRepository gitRepository;
  @Autowired
  @Qualifier("codeAnalyzer")
  private CodeAnalyzer codeAnalyzer;

  @Override
  public void onApplicationEvent(final CodeRepoEvent codeRepoEvent) {
    CodeRepo codeRepo = (CodeRepo) codeRepoEvent.getSource();
    if (codeRepo.getStatus() != Status.PROCESSING) {
      codeRepo = codeRepoService.changeStatus(codeRepo.getId(), CodeRepo.Status.PROCESSING);
      System.out.println("repo status changed to processing");
      try {
        System.out.println("going to checkout the repo");
        final GitRepo gitRepo = gitRepository.getOrClone(codeRepo);
        System.out.println("repo has been checked out successfully");
        gitRepo.pull();
        System.out.println("going to analyze the repo");
        codeRepo = codeAnalyzer.analyze(gitRepo);
        System.out.println("repo has been analyzed successfully");
        codeRepo.setStatus(Status.COMPLETED);
        codeRepo = codeRepoService.update(codeRepo);
        System.out.println("repo status changed to completed");
      } catch (Exception ex) {
        ex.printStackTrace();
        codeRepoService.changeStatus(codeRepo.getId(), CodeRepo.Status.FAILED);
        System.out.println("repo status changed to failed");
      }
    }
  }
}
