package com.crossover.interceptor;

import com.crossover.dto.ErrorResponse;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class ConstraintViolationExceptionHandler implements
    ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(final ConstraintViolationException ex) {
    return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(
        ex.getConstraintViolations().stream()
            .map(constraintViolation -> String
                .format("%s %s", constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getMessage())).collect(
            Collectors.toList()))).type(MediaType.APPLICATION_JSON).build();
  }
}