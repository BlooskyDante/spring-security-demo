package com.luv2code.springboot.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

	public static final String EMPLOYEE = "EMPLOYEE";
	public static final String MANAGER = "MANAGER";
	public static final String ADMIN = "ADMIN";
	public static final String PATH_EMPLOYEES = "/api/employees";
	public static final String PATH_EMPLOYEES_STAR = "/api/employees/**";

	@Bean
	public UserDetailsManager userDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

		manager.setUsersByUsernameQuery("select user_id, pw, active from members where user_id=?");
		manager.setAuthoritiesByUsernameQuery("select user_id, role from roles where user_id=?");

		return manager;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						configurer ->
								configurer
										.requestMatchers(HttpMethod.GET, PATH_EMPLOYEES)
										.hasRole(EMPLOYEE)
										.requestMatchers(HttpMethod.GET, PATH_EMPLOYEES_STAR)
										.hasRole(EMPLOYEE)
										.requestMatchers(HttpMethod.POST, PATH_EMPLOYEES)
										.hasRole(MANAGER)
										.requestMatchers(HttpMethod.PUT, PATH_EMPLOYEES)
										.hasRole(MANAGER)
										.requestMatchers(HttpMethod.DELETE, PATH_EMPLOYEES_STAR)
										.hasRole(ADMIN));
		return http.build();
	}

	//    @Bean
	//    public InMemoryUserDetailsManager userDetailsManager() {
	//        UserDetails john = User.builder()
	//                .username("John")
	//                .password("{noop}test123")
	//                .roles(EMPLOYEE)
	//                .build();
	//
	//        UserDetails mary = User.builder()
	//                .username("Mary")
	//                .password("{noop}test123")
	//                .roles(EMPLOYEE, MANAGER)
	//                .build();
	//
	//        UserDetails susan = User.builder()
	//                .username("Susan")
	//                .password("{noop}test123")
	//                .roles(EMPLOYEE, MANAGER, ADMIN)
	//                .build();
	//
	//        return new InMemoryUserDetailsManager(john, mary, susan);
	//    }

}
