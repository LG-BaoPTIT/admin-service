package com.ite.adminservice.security.jwt;


import com.ite.adminservice.entities.Admin;
import com.ite.adminservice.repositories.AdminRepository;
import com.ite.adminservice.security.CustomAdminDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Configuration
public class CustomFilter extends OncePerRequestFilter {


    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CustomAdminDetailsService customAdminDetailsService;

    @Autowired
    private RestTemplate restTemplate;

    private Claims claims = null;
    private String userName = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Optional<String> authorizationHeader = Optional.ofNullable(request.getHeader("Authorization"));
        if(authorizationHeader.isPresent() && authorizationHeader.get().startsWith("Bearer ")){
            String id = request.getHeader("id");


            Admin admin = adminRepository.findAdminByAdminId(id);
            UserDetails userDetails = customAdminDetailsService.loadUserByUsername(admin.getEmail());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }


    public String getCurrentUser(){

        return this.userName;
    }

}










//            token = authorizationHeader.get().substring(7);
//            try{
//                TokenDTO tokenDTO = TokenDTO.builder()
//                        .token(token)
//                        .build();
////
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//
//                String checkTokenUrl = "http://localhost:9009/admin/checkAccessToken";
//
//                ResponseEntity<CheckTokenResponse> checkTokenResponse = restTemplate.exchange(
//                        checkTokenUrl,
//                        HttpMethod.POST,
//                        new HttpEntity<>(tokenDTO, headers),
//                        CheckTokenResponse.class
//                );
//
//
//                CheckTokenResponse tokenResponse = checkTokenResponse.getBody();
//
//                if (checkTokenResponse.getStatusCode().equals(HttpStatus.OK) && tokenResponse != null) {
//
//                    String email = tokenResponse.getEmail();
//                    UserDetails userDetails = customAdminDetailsService.loadUserByUsername(email);
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                }


//            }catch (HttpClientErrorException e) {
//                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                    logger.warn("Unauthorized access token");
//                } else {
//                    logger.error("Failed to validate access token");
//                }
//            }





//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if(request.getServletPath().matches("/user/login|/user/signup|/user/forgotPassword")){
//            filterChain.doFilter(request,response);
//        }else{
//
//            String authorizationHeader = request.getHeader("Authorization");
//            String token = null;
//
//            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
//                token = authorizationHeader.substring(7);
//                userName = jwtUtil.extractUsername(token);
//                claims = jwtUtil.extractAllClaims(token);
//            }
//            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
//                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
//                if(jwtUtil.validateToken(token)){
//                    System.out.println("Roles from token: " + claims.get("role"));
//                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//
//                    usernamePasswordAuthenticationToken.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                }
//            }
//            filterChain.doFilter(request,response);
//        }
//
//
//    }
//    public boolean isSystemRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_SYSTEM.name().equalsIgnoreCase(roleName);
//    }
//
//    public boolean isManagerRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_MANAGER.name().equalsIgnoreCase(roleName);
//    }
//
//    public boolean isStaffRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_STAFF.name().equalsIgnoreCase(roleName);
//    }
//
//    public boolean isClientRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_CLIENT.name().equalsIgnoreCase(roleName);
//    }
//
//    public boolean isGuestRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_GUEST.name().equalsIgnoreCase(roleName);
//    }
//