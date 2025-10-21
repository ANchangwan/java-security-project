package com.example.security_project.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.security_project.dto.MemberDto;
import com.example.security_project.utils.JwtUtils;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        String authHeader  = request.getHeader("Authorization");

        log.info("Authorization : {}",authHeader);

        try{
            // Bearer[공백][JWT 토큰]
            String accessToken = authHeader.substring(7);

            Map<String, Object> claims = JwtUtils.validateToken(accessToken);
            log.info("claims : {}", claims);

            String email = (String)claims.get("email");
            String password = (String)claims.get("password");
            String nickName = (String)claims.get("nickName");
            List<String> roleName =(List<String>)claims.get("roleNames");

            MemberDto memberDto = new MemberDto(email, password, nickName, roleName);

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberDto, password, memberDto.getAuthorities());;

            

                filterChain.doFilter(request, response);

        } catch(Exception ex){
            log.error("error : {}", ex.getMessage());
            Throwable cause = ex.getCause();
                    if (cause instanceof AccessDeniedException) {
                        throw new AccessDeniedException("에러발생");
                    }else{
                        Gson gson = new Gson();
                        String jsonStr = gson.toJson(Map.of("error","ERROR_ACCESS_TOKEN"));

                        response.setContentType("application/json; charset=utf-8");
                        PrintWriter pw = response.getWriter();
                        pw.println(jsonStr);
                        pw.close();
                }
            }
        }
    

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
        String path = request.getRequestURI();
        String method = request.getMethod();
        log.info("url : {}", path);
        log.info("HTTP method : {}", method);

        if(method.equalsIgnoreCase("OPTIONS") || path.equals("/api/v1/members/login") ){
            return true;
        }
        return false; //jwtcheckfilter 필터 적용
    } 


    
}
