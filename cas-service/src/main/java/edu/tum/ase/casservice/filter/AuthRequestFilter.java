package edu.tum.ase.casservice.filter;

import edu.tum.ase.casservice.jwt.JwtUtil;
import edu.tum.ase.casservice.model.AseUser;
import edu.tum.ase.casservice.repository.UserRepository;
import edu.tum.ase.casservice.service.AuthService;
import edu.tum.ase.casservice.service.MongoUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthRequestFilter extends OncePerRequestFilter {
    @Autowired
    private MongoUserDetailsService mongoUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String username = null;
        String jwt = null;
        // role ?

//        final String authHeader = request.getHeader("Authorization");
//        System.out.println("Authenticate Header " + authHeader);

        // read jwt from cookie every requesting
        Map<String, Object> map = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        // jwt
        if (cookies!=null){

            for (Cookie cookie : cookies) {
                if (cookie != null) {
                    String name=cookie.getName();
                    String value = cookie.getValue();
                    map.put(name,value);
                }
            }
            if(map.containsKey("jwt")){
                jwt=map.get("jwt").toString();
                try {
                    if (jwtUtil.verifyJwtSignature(jwt)) {
                        username = jwtUtil.extractUsername(jwt);
                    }
                } catch (ExpiredJwtException e) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "JWT expires or not signed by us");
                    return;
                } catch (Exception e) {
                    System.err.println("Unmatched JWT Signature");
                    response.sendError(HttpStatus.EXPECTATION_FAILED.value(), "Unmatched signature");
                    return;
                }

            }
//            else {
//                if (!authHeader.startsWith("Basic")) {
//                    response.sendError(HttpStatus.BAD_REQUEST.value(), "No JWT Token or Basic Auth Info Found");
//                }
//            }

        }

//        if(map.containsKey("jwt")){
//
//        if (jwt!= null) {
//            try {
//                if (jwtUtil.verifyJwtSignature(jwt)) {
//                    username = jwtUtil.extractUsername(jwt);
//                }
//            } catch (ExpiredJwtException e) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "JWT expires or not signed by us");
//                return;
//            } catch (Exception e) {
//                System.err.println("Unmatched JWT Signature");
//                response.sendError(HttpStatus.EXPECTATION_FAILED.value(), "Unmatched signature");
//                return;
//            }
//        } else {
//            if (!authHeader.startsWith("Basic")) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "No JWT Token or Basic Auth Info Found");
//            }
//        }}



//        //valid authentication
//        if (authHeader != null && authHeader.startsWith("Bearer")) {
//            // TODO: Get the JWT in the header.
//            // 'Authorization: Bearer <JWT_TOKEN>'
//            jwt=authHeader.substring("Bearer".length()).trim();
//            // If the JWT expires or not signed by us, send an error to the client
//            if( jwtUtil.verifyJwtSignature(jwt)==false) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "JWT expires or not signed by us");
//            }
//
//            // TODO: Extract the username from the JWT token.
//            username=jwtUtil.extractUsername(jwt);
//        } else {
//            // No valid authentication, No go
//            if (!authHeader.startsWith("Basic")) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "No JWT Token or Basic Auth Info Found");
//            }
//        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // TODO: load a user from the database that has the same username as in the JWT token.

            AseUser user=userRepository.getAseUserByUsername(username);
            User userDetails =user.getUser();
            authService.setAuthentication(userDetails, request);
            Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(String.format("Authenticate Token Set:\n"
                            + "Username: %s\n"
                            + "Password: %s\n"
                            + "Authority: %s\n",
                    authContext.getPrincipal(),
                    authContext.getCredentials(),
                    authContext.getAuthorities().toString()));
        }
        filterChain.doFilter(request, response);
    }
}