package com.crossover.interceptor;

import com.crossover.dto.ErrorResponse;
import java.util.Arrays;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class IllegalArgumentExceptionHandler implements ExceptionMapper<IllegalArgumentException> {

  @Override
  public Response toResponse(final IllegalArgumentException ex) {
    return Response.status(Status.BAD_REQUEST)
        .entity(new ErrorResponse(Arrays.asList(ex.getMessage()))).type(MediaType.APPLICATION_JSON)
        .build();
  }
}
