package com.crossover.config;

import com.crossover.api.RepositoryController;
import com.crossover.interceptor.ClientErrorHandler;
import com.crossover.interceptor.ConstraintViolationExceptionHandler;
import com.crossover.interceptor.DataIntegrityViolationExceptionHandler;
import com.crossover.interceptor.ExceptionHandler;
import com.crossover.interceptor.IllegalArgumentExceptionHandler;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import javax.annotation.PostConstruct;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebConfig extends ResourceConfig {

  @Value("${spring.jersey.applicationPath:/}")
  private String apiPath;

  public WebConfig() {
    registerEndpoints();
  }

  @PostConstruct
  public void init() {
    initSwagger();
  }

  private void registerEndpoints() {
    register(RepositoryController.class);
    register(ExceptionHandler.class);
    register(ConstraintViolationExceptionHandler.class);
    register(IllegalArgumentExceptionHandler.class);
    register(DataIntegrityViolationExceptionHandler.class);
    register(ClientErrorHandler.class);
  }

  private void initSwagger() {
    register(ApiListingResource.class);
    register(SwaggerSerializers.class);
    BeanConfig config = new BeanConfig();
    config.setConfigId("dead-code-detection");
    config.setTitle("Dead code Detection");
    config.setVersion("v1");
    config.setContact("Waqas Shahid");
    config.setSchemes(new String[]{"http", "https"});
    config.setBasePath(this.apiPath);
    config.setResourcePackage("com.crossover.api");
    config.setPrettyPrint(true);
    config.setScan(true);
  }
}