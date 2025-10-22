package com.example.security_project.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.security_project.dto.MemberDto;
import com.example.security_project.utils.JwtUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        log.info("uri : {}", path);
        log.info("HTTP method : {}", method);

        if (method.equalsIgnoreCase("OPTIONS") || path.equals("/api/v1/members/login")
                || path.startsWith("/api/v1/refresh")) {
            return true; // JWTCheckFilter 필터를 적욯하지 않음
        }
        return false; // JWTCheckFilter 필터를 적용
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        log.info("Authorization Header : {}", authHeader);

        try {
            // Bearer[공백][JWT 토큰]
            String accessToken = authHeader.substring(7);

            Map<String, Object> claims = JwtUtil.validateToken(accessToken);
            log.info("claims : {}", claims);

            String email = (String) claims.get("email");
            String password = (String) claims.get("password");
            String nickName = (String) claims.get("nickName");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDto memberDto = new MemberDto(email, password, nickName, roleNames);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDto,
                    password, memberDto.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("error : {}", ex.getMessage());

            Throwable cause = ex.getCause();
            if (cause instanceof AccessDeniedException) {
                throw (AccessDeniedException) cause;
            } else {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(Map.of("error", "ERROR_ACESS_TOKEN"));

                response.setContentType("application/json; charset=utf-8");
                PrintWriter pw = response.getWriter();
                pw.println(jsonStr);
                pw.close();
            }
        }
    }
}