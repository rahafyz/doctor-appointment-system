package com.example.doctorappointment.security;

import com.example.doctorappointment.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@AllArgsConstructor
public class Filter extends OncePerRequestFilter {


    private final JwtBuilder jwtTokenUtil;
    private final AntPathMatcher pathMatcher;

    UserDetail userDetail;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtTokenUtil.validateToken(requestTokenHeader)) {
            try {
                Claims body = jwtTokenUtil.getAllClaimsFromToken(requestTokenHeader);

                userDetail.setUsername(body.getSubject());
                userDetail.setUserId(Long.parseLong((String) body.get("userId")));


            } catch (JwtException | ClassCastException e) {
                throw new CustomException(e.getMessage(), HttpStatus.FORBIDDEN);
            }

            chain.doFilter(request, response);
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return "/login".equals(path) || "/register".equals(path)|| pathMatcher.match("/swagger-ui/**",path);
    }
}

