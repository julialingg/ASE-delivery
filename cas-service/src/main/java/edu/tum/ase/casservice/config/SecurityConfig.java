package edu.tum.ase.casservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tum.ase.casservice.service.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*",allowCredentials = "true")
public class SecurityConfig   {
    @Autowired
    MongoUserDetailsService mongoUserDetailsService;

    //    @Override
//    public void configure(AuthenticationManagerBuilder builder) throws Exception {
//        builder.userDetailsService(mongoUserDetailsService);
//    }
//
    @Bean
    public UserDetailsService userDetailsService() {
        return new MongoUserDetailsService();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .formLogin() ;// 1. Use Spring Login Form
        http.cors()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            .csrf().disable() // Note: for demonstration purposes only, this should not be done

                .and()
                .authorizeRequests() // 2. Require authentication in all endpoints except login
                .antMatchers(HttpMethod.GET,"/auth/**").permitAll()
//            .antMatchers("/**").authenticated()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .httpBasic() // 3. Use Basic Authentication
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .sessionManagement().disable();

        return http.build();
    }



    @Bean
    AuthenticationManager myAuthenticationManager(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider::authenticate;
    }
//    @Override
//    @Bean
//// Define an authentication manager to execute authentication services
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//
//        return super.authenticationManagerBean();
//    }

    @Bean
// Define an instance of Bcrypt for hashing passwords
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        HashMap<String, String> map = new HashMap<>(2);
        return (request, response, exception) -> {
            if (exception instanceof BadCredentialsException) {
//                map.put("timestamp",new Date().toString());
//                map.put("error", "Username or password is wrong");
//                map.put("path", request.getRequestURI());
//                map.put("status","401");
//
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                String rep="Username or password is wrong";

//                ObjectMapper objectMapper = new ObjectMapper();
//                String resBody = objectMapper.writeValueAsString(map);
                response.setContentLength(rep.length());    // set length
                PrintWriter printWriter = response.getWriter();
                printWriter.print(rep);
                printWriter.flush();
                printWriter.close();
            } else {
//                response.setHeader("Access-Control-Allow-Origin","*");
                response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");

                response.setHeader("Access-Control-Allow-Credentials","true");
                response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "5000");
                response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,Authorization,Token");
                response.setHeader("WWW-Authenticate","Basic realm=\"Realm\"");
                map.put("timestamp",new Date().toString());
                map.put("error", "unauthorized");
                map.put("path", request.getRequestURI());
                map.put("status","401");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ObjectMapper objectMapper = new ObjectMapper();
                String resBody = objectMapper.writeValueAsString(map);
                response.setContentLength(resBody.length());    // set length
                PrintWriter printWriter = response.getWriter();
                printWriter.print(resBody);
                printWriter.flush();
                printWriter.close();
            }
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        };
    }
}
