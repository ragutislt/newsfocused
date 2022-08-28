package eu.adainius.newsfocused.admin.site.back.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;

@WebServlet(name = "StaticResourceServlet", description = "Serves requests of static resources - js, css, etc...", urlPatterns = {
        "/static/*" })
@Slf4j
public class StaticResourceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Received a request for {}", req.getRequestURI());

        String fileName = StringUtils.substringAfterLast(req.getRequestURI(), "static/");
        String fileContentToServe;

        try {

            InputStream resource = new ClassPathResource(String.format("public/static/%s", fileName)).getInputStream();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource))) {
                fileContentToServe = reader.lines()
                        .collect(Collectors.joining("\n"));
            }

            PrintWriter responseBodyWriter = resp.getWriter();
            resp.setContentType(this.getServletContext().getMimeType(fileName));
            resp.setCharacterEncoding("UTF-8");
            responseBodyWriter.print(fileContentToServe);
            responseBodyWriter.flush();
        } catch (IOException e) {
            // TODO implement custom error page
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
