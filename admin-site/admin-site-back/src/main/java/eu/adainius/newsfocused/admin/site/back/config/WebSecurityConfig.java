package eu.adainius.newsfocused.admin.site.back.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Slf4j
public class WebSecurityConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(
				User.withDefaultPasswordEncoder().username("user").password("password").roles(Roles.ROLE_ADMIN)
						.build());
		log.info("initializing userDetailsService");
		return manager;
	}

	private class BasicAuthenticationEntryPointNonHtml implements AuthenticationEntryPoint {
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException {
			response.addHeader("WWW-Authenticate", "Basic realm=Newsfocused Admin Site");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
	}

	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		return new BasicAuthenticationEntryPointNonHtml();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.mvcMatchers("/admin/api/**")
				.authenticated()
				.and()
				.anonymous().disable()
				.httpBasic()
				.authenticationEntryPoint(authenticationEntryPoint())
				.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain unauthenticatedFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/admin/index").permitAll();
		return http.build();
	}

	private AccessDeniedHandler accessDeniedHandler() {
		AccessDeniedHandler handler = new AccessDeniedHandler() {
			@Override
			public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
					AccessDeniedException e) throws IOException, ServletException {
				log.info("AccessDeniedHandler is reached");
				httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			}
		};
		return handler;
	}
}