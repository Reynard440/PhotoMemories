package com.photomemories.web.sb.config;

import com.photomemories.web.sb.filter.CustomAuthFilter;
import com.photomemories.web.sb.filter.CustomAuthorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@PropertySource(value = "classpath:application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value(value = "${security.token.secret}")
    public String secret;

    @Autowired
    public SecurityConfig (UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthFilter customAuthFilter = new CustomAuthFilter(authenticationManagerBean());
        CustomAuthorFilter customAuthorFilter = new CustomAuthorFilter();
        customAuthFilter.setFilterProcessesUrl("/v1/c1/login");
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/v1/c1/login").permitAll();
        http.authorizeRequests().antMatchers("/v1/c1/addNewUser").permitAll();

        http.authorizeRequests().antMatchers(GET, "/v1/c1/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(POST, "/v1/c1/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(PUT, "/v1/c1/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(DELETE, "/v1/c1/**").hasAnyAuthority("USER_ROLE");

        http.authorizeRequests().antMatchers(GET, "/v1/c2/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(POST, "/v1/c2/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(PUT, "/v1/c2/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(DELETE, "/v1/c2/**").hasAnyAuthority("USER_ROLE");

        http.authorizeRequests().antMatchers(GET, "/v1/c3/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(POST, "/v1/c3/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(PUT, "/v1/c3/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(DELETE, "/v1/c3/**").hasAnyAuthority("USER_ROLE");

        http.authorizeRequests().antMatchers(GET, "/v1/c4/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/v1/c4/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(PUT, "/v1/c4/**").hasAnyAuthority("USER_ROLE");
        http.authorizeRequests().antMatchers(DELETE, "/v1/c4/**").hasAnyAuthority("USER_ROLE");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthFilter);
        http.addFilterBefore(customAuthorFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
