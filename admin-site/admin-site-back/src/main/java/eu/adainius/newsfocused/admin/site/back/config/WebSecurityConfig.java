package eu.adainius.newsfocused.admin.site.back.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@EnableWebSecurity
public class WebSecurityConfig {

	private static final String ROLE_ADMIN = "ADMIN";

    @Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles(ROLE_ADMIN).build());
		return manager;
	}

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint authenticationEntryPoint = new BasicAuthenticationEntryPoint();
        authenticationEntryPoint.setRealmName("Newsfocused Admin Site");
        return authenticationEntryPoint;
    }

    @Bean
	@Order(1)                                                        
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http
			.antMatcher("/admin/api/**")                             
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest().hasRole(ROLE_ADMIN)
			)
			.httpBasic()
            .authenticationEntryPoint(authenticationEntryPoint())
            .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		return http.build();
	}

    private AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandler handler = new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        };
        return handler;
    }

	// @Bean                                                            
	// public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
	// 	http
	// 		.authorizeHttpRequests(authorize -> authorize
	// 			.anyRequest().authenticated()
	// 		)
	// 		.formLogin(withDefaults());
	// 	return http.build();
	// }
}