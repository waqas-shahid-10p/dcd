package com.crossover.interceptor;

import com.crossover.dto.SuccessResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class DataIntegrityViolationExceptionHandler implements
    ExceptionMapper<DataIntegrityViolationException> {

  @Override
  public Response toResponse(final DataIntegrityViolationException ex) {
    return Response.status(Status.CONFLICT).entity(new SuccessResponse(ex.getMessage()))
        .type(MediaType.APPLICATION_JSON).build();
  }
}
