package com.raptors.dashboard.config;

import com.raptors.dashboard.security.CustomAuthenticationProvider;
import com.raptors.dashboard.security.JWTAuthenticationFilter;
import com.raptors.dashboard.security.JWTAuthorizationFilter;
import com.raptors.dashboard.security.Role;
import com.raptors.dashboard.security.SecurityPropertyHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

import static com.raptors.dashboard.security.Role.ROLE_ADMIN;
import static com.raptors.dashboard.security.Role.ROLE_OWNER;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final SecurityPropertyHolder securityPropertyHolder;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final int managementPort;

    public WebSecurity(SecurityPropertyHolder securityPropertyHolder,
                       CustomAuthenticationProvider customAuthenticationProvider,
                       @Value("${management.server.port}") int managementPort) {
        this.securityPropertyHolder = securityPropertyHolder;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.managementPort = managementPort;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .requestMatchers(forPortAndPath(managementPort, "/dashboard/register")).permitAll()
                .antMatchers("/admin/**").hasRole(adminRole())
                .antMatchers("/user/**").hasAnyRole(ownerRole(), adminRole())
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), securityPropertyHolder))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), securityPropertyHolder))
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED));
    }

    private RequestMatcher forPortAndPath(final int port, final String pathPattern) {
        return new AndRequestMatcher(forPort(port), new AntPathRequestMatcher(pathPattern));
    }

    private RequestMatcher forPort(final int port) {
        return (HttpServletRequest request) -> port == request.getLocalPort();
    }

    private String adminRole() {
        return getRoleSuffix(ROLE_ADMIN);
    }

    private String ownerRole() {
        return getRoleSuffix(ROLE_OWNER);
    }

    private String getRoleSuffix(Role role) {
        return StringUtils.substringAfter(role.name(), "_");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

}
