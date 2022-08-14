package eu.adainius.newsfocused.admin.site.back.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@WebServlet(
  name = "StaticResourceServlet",
  description = "Serves requests of static resources - js, css, etc...",
  urlPatterns = {"/static/*"}
)
@Slf4j
public class StaticResourceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Received a request for {}", req.getRequestURI());
        // do something
    }
}
