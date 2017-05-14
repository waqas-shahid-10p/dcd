package com.crossover.api;

import com.crossover.dto.RepoDTO;
import com.crossover.event.PaginatedResultsRetrievedEvent;
import com.crossover.model.CodeRepo;
import com.crossover.model.DeadCodeOccurrence;
import com.crossover.service.base.CodeRepoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

@Path("/repositories")
@Api(value = "/repositories", description = "APIs for Repository")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RepositoryController {

  @Autowired
  private MessageSource messageSource;
  @Autowired
  private CodeRepoService codeRepoService;
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @GET
  @ApiResponses(value = {
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "list of repos"),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Size must be greater than 0")})
  public List<CodeRepo> list(@QueryParam("page") final int page, @QueryParam("size") final int size,
      @Context final HttpServletRequest request, @Context final HttpServletResponse response) {
    return paginate(page, size, request, response, codeRepoService.list(page, size));
  }

  @GET
  @Path("/{id:\\d+}")
  @ApiResponses(value = {
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "returns the repo object"),
      @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "repo not found with id {id}")})
  public CodeRepo get(@PathParam("id") @Nonnull final long id) {
    return codeRepoService.get(id);
  }

  @POST
  @ApiResponses(value = {
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "request body is not valid"),
      @ApiResponse(code = HttpServletResponse.SC_ACCEPTED, message = "returns the newly created repo")})
  public Response create(@Nonnull final RepoDTO repoDTO) {
    final CodeRepo codeRepo = codeRepoService.create(repoDTO);
    return Response.accepted().entity(codeRepo).type(MediaType.APPLICATION_JSON).build();
  }

  @PUT
  @Path("/{id:\\d+}/analysis")
  @ApiResponses(value = {
      @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "repo not found with id {id}"),
      @ApiResponse(code = HttpServletResponse.SC_ACCEPTED, message = "")})
  public Response analyze(@PathParam("id") @Nonnull final long id) {
    codeRepoService.analyze(id);
    return Response.accepted().type(MediaType.APPLICATION_JSON).build();
  }

  @GET
  @Path("/{id:\\d+}/analysis/deadcode")
  @ApiResponses(value = {
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "list of dead code occurrences"),
      @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "repo not found with id {id}"),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Size must be greater than 0")})
  public List<DeadCodeOccurrence> getDeadCodeOccurrences(@PathParam("id") @Nonnull final long id,
      @QueryParam("page") final int page, @QueryParam("size") final int size,
      @Context final HttpServletRequest request, @Context final HttpServletResponse response) {
    return paginate(page, size, request, response,
        codeRepoService.getDeadCodeOccurrences(id, page, size));
  }

  private <T> List<T> paginate(int page, int size, HttpServletRequest request,
      HttpServletResponse response, Page<T> pageContent) {
    if (page <= pageContent.getTotalPages()) {
      eventPublisher.publishEvent(
          new PaginatedResultsRetrievedEvent<>(RepositoryController.class,
              UriComponentsBuilder.fromUriString(request.getRequestURL().toString()), response,
              page,
              pageContent.getTotalPages(), size));
    }
    return pageContent.getContent();
  }
}