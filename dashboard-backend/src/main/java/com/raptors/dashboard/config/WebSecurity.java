package com.raptors.dashboard.config;

import com.raptors.dashboard.security.CustomAuthenticationProvider;
import com.raptors.dashboard.security.JWTAuthenticationFilter;
import com.raptors.dashboard.security.JWTAuthorizationFilter;
import com.raptors.dashboard.security.SecurityPropertyHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final SecurityPropertyHolder securityPropertyHolder;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public WebSecurity(SecurityPropertyHolder securityPropertyHolder,
                       CustomAuthenticationProvider customAuthenticationProvider) {
        this.securityPropertyHolder = securityPropertyHolder;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("admin/**").hasRole("ADMIN")
                .antMatchers("user/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), securityPropertyHolder))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), securityPropertyHolder))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addExposedHeader(AUTHORIZATION);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
