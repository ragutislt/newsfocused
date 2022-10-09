package eu.adainius.newsfocused.admin.site.back.controller;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.services.AdminSiteApplicationService;
import lombok.AllArgsConstructor;

@Component
@Path("api")
@AllArgsConstructor
public class ApiController {
    private AdminSiteApplicationService adminSiteApplicationService;

    @Path("user/search")
    @GET
    @Produces("application/json")
    public Set<User> searchUser(@QueryParam("term") String emailSearchTerm, @QueryParam("pageSize") int pageSize, @QueryParam("pageRequested") int pageRequested) {
        String adminUsername = "";
        //return adminSiteApplicationService.searchUser(adminUsername, emailSearchTerm, pageSize, pageRequested);
        return null;
    }

    @Path("user")
    @POST
    @Produces("application/json")
    public User registerUser(RegisterUserRequest request) {
        String adminUsername = "";
        //return adminSiteApplicationService.searchUser(adminUsername, emailSearchTerm, pageSize, pageRequested);
        return null;
    }

    @Path("user")
    @PUT
    @Produces("application/json")
    public User updateUser(RegisterUserRequest request) {
        String adminUsername = "";
        //return adminSiteApplicationService.searchUser(adminUsername, emailSearchTerm, pageSize, pageRequested);
        return null;
    }
}
