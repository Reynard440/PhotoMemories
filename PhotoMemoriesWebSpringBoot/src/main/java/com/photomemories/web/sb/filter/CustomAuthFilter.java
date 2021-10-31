package com.photomemories.web.sb.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthFilter.class);
    private final AuthenticationManager authenticationManager;

    @Autowired
    public CustomAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("UserHashPassword");
        LOGGER.info("Email is {} and password is {} ", email, password);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        User user = (User)auth.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("CMPG323".getBytes());

        String access_token = getAccessToken(request, user, algorithm);

        String refresh_token = getRefreshToken(request, user, algorithm);

        Map<String, String> coins = mappingAccessAndRefreshTokens(response, access_token, refresh_token);
        new ObjectMapper().writeValue(response.getOutputStream(), coins);
    }

    private String getAccessToken(HttpServletRequest request, User user, Algorithm algorithm) {
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }

    private String getRefreshToken(HttpServletRequest request, User user, Algorithm algorithm) {
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        return refresh_token;
    }

    private Map<String, String> mappingAccessAndRefreshTokens(HttpServletResponse response, String access_token, String refresh_token) {
        Map<String, String> coins = new HashMap<>();
        coins.put("access_token", access_token);
        coins.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        return coins;
    }

}
