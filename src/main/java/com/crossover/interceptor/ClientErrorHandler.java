package com.crossover.interceptor;

import com.crossover.dto.ErrorResponse;
import java.util.Arrays;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ClientErrorHandler implements ExceptionMapper<ClientErrorException> {

  @Autowired
  private MessageSource messageSource;

  @Override
  public Response toResponse(final ClientErrorException ex) {
    return Response.status(ex.getResponse().getStatus())
        .entity(new ErrorResponse(Arrays.asList(ex.getMessage()))).type(MediaType.APPLICATION_JSON)
        .build();
  }
}
