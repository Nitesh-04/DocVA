package com.example.docva.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class Middleware extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public Middleware(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,   // like express (req, res, next)
                                    FilterChain filterChain)
        throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/auth") || path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {  // if not authenticated already

            if(jwtUtil.validateToken(token, username)) {
                UsernamePasswordAuthenticationToken authToken =       // spring way of saving username, password, roles
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));   // stores IP etc. for audit and logs

                SecurityContextHolder.getContext().setAuthentication(authToken); // global context to get user data anywhere (thread level)
            }
        }

        filterChain.doFilter(request, response);  // go to next
    }
}
