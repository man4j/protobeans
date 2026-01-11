package org.protobeans.webapp.example.security;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class TokenFormRouterFilter extends OncePerRequestFilter {
    private final String signinOttPath = "/signin_ott";
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        
        if (uri.equals(signinOttPath)) {
            return true;
        }
        
        return !"GET".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            // пользователь не залогинен — пусть обычная форма логина отрабатывает
            filterChain.doFilter(request, response);
            return;
        }

        Set<String> authorities = auth.getAuthorities()
                                      .stream()
                                      .map(GrantedAuthority::getAuthority)
                                      .collect(Collectors.toSet());

        boolean hasPassword = authorities.contains(FactorGrantedAuthority.PASSWORD_AUTHORITY);
        boolean hasOtt = authorities.contains(FactorGrantedAuthority.OTT_AUTHORITY);

        if (hasPassword && !hasOtt) {
            // важно: учитывай contextPath
            String target = request.getContextPath() + signinOttPath;
            response.sendRedirect(target);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}
