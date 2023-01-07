package eu.adainius.newsfocused.admin.site.back.infrastructure.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;

import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.UserSearchResults;
import eu.adainius.newsfocused.admin.site.back.domain.services.AdminSiteDomainService;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@Controller
@Path("api")
@AllArgsConstructor
public class ApiController {
    private AdminSiteDomainService adminSiteApplicationService;

    // TODO for the search, we should only return a limited set of fields
    @Path("user/search")
    @GET
    @Produces("application/json")
    public UserSearchResults searchUser(
            @QueryParam("term") String emailSearchTerm,
            @QueryParam("pageSize") int pageSize,
            @QueryParam("pageRequested") int pageRequested,
            @Context HttpServletRequest httpRequest) {

        String adminUsername = httpRequest.getUserPrincipal().getName();

        Either<String, UserSearchResults> result = adminSiteApplicationService.searchUser(adminUsername,
                emailSearchTerm, pageSize, pageRequested);

        if (result.isRight()) {
            return result.get();
        } else {
            throw new WebApplicationException(result.getLeft(), Response.Status.BAD_REQUEST);
        }
    }

    @Path("user")
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public User registerUser(RegisterUserRequest request, @Context HttpServletRequest httpRequest) {

        String adminUsername = httpRequest.getUserPrincipal().getName();

        Either<String, User> result = adminSiteApplicationService.registerUser(adminUsername, request.getEmail(),
                request.getDaysToSendOn(), request.getSites(), request.getHeadlineCount());

        if (result.isRight()) {
            return result.get();
        } else {
            throw new WebApplicationException(result.getLeft(), Response.Status.BAD_REQUEST);
        }
    }

    @Path("user")
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    public User updateUser(RegisterUserRequest request, @Context HttpServletRequest httpRequest) {

        String adminUsername = httpRequest.getUserPrincipal().getName();

        Either<String, User> result = adminSiteApplicationService.updateUser(adminUsername, request.getEmail(),
                request.getDaysToSendOn(), request.getSites(), request.getHeadlineCount());

        if (result.isRight()) {
            return result.get();
        } else {
            throw new WebApplicationException(result.getLeft(), Response.Status.BAD_REQUEST);
        }
    }

    @Path("user/{email}")
    @GET
    @Produces("application/json")
    public User retrieveUser(
            @PathParam("email") String userEmail,
            @Context HttpServletRequest httpRequest) {
        String adminUsername = httpRequest.getUserPrincipal().getName();

        Either<String, Optional<User>> result = adminSiteApplicationService.retrieveUser(adminUsername,
                userEmail);

        if (result.isRight()) {
            return result.get()
                    .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        } else {
            throw new WebApplicationException(result.getLeft(), Response.Status.BAD_REQUEST);
        }
    }
}
