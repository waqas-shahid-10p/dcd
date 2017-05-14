package com.crossover.code;

import com.crossover.exception.AnalyzerException;
import com.crossover.model.DeadCodeOccurrence;
import com.crossover.model.GitRepo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier("deadCodeChecker")
public class DeadCodeAnalyzer extends UnderstandAnalyzer<List<DeadCodeOccurrence>> {

  private static final String UNUSED_PERL_SCRIPT = "uperl %s/acjf_unused.pl -db=%s -bykind -kind=%s";
  private static final Pattern INSTANCE_PATTERN = Pattern
      .compile("File:(.*) Line:(\\s\\d+(-\\d+)?)");
  private static final String[] KINDS = new String[]{
      "Variable",
      "Method",
      "Parameter",
      "Class"
  };
  @Value("${SCITOOLS_HOME:/home/waqas/Downloads/scitools}")
  private String sciToolsPath;
  private String scriptsPath;

  @PostConstruct
  private void afterConstruct() {
    scriptsPath = sciToolsPath + "/scripts/perl";
  }

  @Override
  public List<DeadCodeOccurrence> analyze(final GitRepo gitRepo) throws AnalyzerException {
    final List<DeadCodeOccurrence> deadCodeOccurrences = new LinkedList<>();
    for (final String kind : KINDS) {
      try {
        final Process undCommand = Runtime.getRuntime()
            .exec(String
                .format(UNUSED_PERL_SCRIPT, scriptsPath, getDatabasePath(gitRepo), kind));
        final BufferedReader reader = new BufferedReader(
            new InputStreamReader(undCommand.getInputStream()));
        String line, currentKind = kind;
        while ((line = reader.readLine()) != null) {
          if (!"".equals(line)) {
            System.out.println(line);
            if (line.indexOf("[File:") == -1) {
              currentKind = line;
            } else {
              final String[] splits = line.split("\\[");
              final DeadCodeOccurrence deadCodeOccurrence = new DeadCodeOccurrence()
                  .withRepo(gitRepo.getCodeRepo()).withValue(splits[0].trim())
                  .withKind(currentKind);
              final Matcher matcher = INSTANCE_PATTERN.matcher(splits[1]);
              if (matcher.find()) {
                deadCodeOccurrence
                    .withFile(matcher.group(1).replace(gitRepo.getDirectory() + "/", ""))
                    .withLine(matcher.group(2));
              }
              deadCodeOccurrences.add(deadCodeOccurrence);
            }
          }
        }
      } catch (IOException ex) {
        throw new AnalyzerException(ex);
      }
    }
    return deadCodeOccurrences;
  }
}