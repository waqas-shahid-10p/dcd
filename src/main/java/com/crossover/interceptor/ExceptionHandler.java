package com.crossover.interceptor;

import com.crossover.dto.ErrorResponse;
import java.util.Arrays;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler implements ExceptionMapper<Throwable> {

  @Autowired
  private MessageSource messageSource;

  @Override
  public Response toResponse(final Throwable ex) {
    ex.printStackTrace();
    return Response.serverError().entity(
        new ErrorResponse(Arrays.asList(messageSource.getMessage("error.default", new Object[]{},
            LocaleContextHolder.getLocale())))).type(MediaType.APPLICATION_JSON).build();
  }
}
