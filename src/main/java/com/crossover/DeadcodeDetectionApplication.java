package com.crossover;

import com.crossover.event.base.AsyncSupportEventMultiCaster;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@SpringBootApplication
public class DeadcodeDetectionApplication {

  public static void main(String[] args) {
    SpringApplication.run(DeadcodeDetectionApplication.class, args);
  }

  @Bean(name = "applicationEventMulticaster")
  public ApplicationEventMulticaster asyncSupportEventMulticaster() {
    AsyncSupportEventMultiCaster eventMulticaster
        = new AsyncSupportEventMultiCaster();

    eventMulticaster.setSyncEventMulticaster(syncApplicationEventMulticaster());
    eventMulticaster.setAsyncEventMulticaster(asyncApplicationEventMulticaster());
    return eventMulticaster;
  }

  @Bean
  public ApplicationEventMulticaster syncApplicationEventMulticaster() {
    return new SimpleApplicationEventMulticaster();
  }

  @Bean
  public ApplicationEventMulticaster asyncApplicationEventMulticaster() {
    SimpleApplicationEventMulticaster eventMulticaster
        = new SimpleApplicationEventMulticaster();
    eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
    return eventMulticaster;
  }
}
