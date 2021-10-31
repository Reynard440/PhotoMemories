package com.photomemories.web.sb.config;

import com.photomemories.web.sb.filter.CustomAuthFilter;
import com.photomemories.web.sb.filter.CustomAuthorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        CustomAuthFilter customAuthFilter = overrideDefaultLoginPath();

        httpBasicConfigurationAndPermission(http);

        controllerOneRestrictions(http);
        controllerTwoRestrictions(http);
        controllerThreeRestrictions(http);
        controllerFourRestrictions(http);

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private CustomAuthFilter overrideDefaultLoginPath() throws Exception {
        CustomAuthFilter customAuthFilter = new CustomAuthFilter(authenticationManagerBean());
        customAuthFilter.setFilterProcessesUrl("/photo-memories/mvc/login");
        return customAuthFilter;
    }

    private void httpBasicConfigurationAndPermission(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/photo-memories/mvc/login/**").permitAll();
    }

    private void controllerOneRestrictions(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(GET, "/photo-memories/mvc/v1/c1/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/photo-memories/mvc/v1/c1/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT, "/photo-memories/mvc/v1/c1/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE, "/photo-memories/mvc/v1/c1/**").hasAnyAuthority("ROLE_USER");
    }

    private void controllerTwoRestrictions(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(GET, "/photo-memories/mvc/v1/c2/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/photo-memories/mvc/v1/c2/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT, "/photo-memories/mvc/v1/c2/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE, "/photo-memories/mvc/v1/c2/**").hasAnyAuthority("ROLE_USER");
    }

    private void controllerThreeRestrictions(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(GET, "/photo-memories/mvc/v1/c3/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/photo-memories/mvc/v1/c3/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT, "/photo-memories/mvc/v1/c3/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE, "/photo-memories/mvc/v1/c3/**").hasAnyAuthority("ROLE_USER");
    }

    private void controllerFourRestrictions(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(GET, "/photo-memories/mvc/v1/c4/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/photo-memories/mvc/v1/c4/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT, "/photo-memories/mvc/v1/c4/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE, "/photo-memories/mvc/v1/c4/**").hasAnyAuthority("ROLE_USER");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
