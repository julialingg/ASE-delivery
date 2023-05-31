package edu.tum.ase.deliveryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*",allowCredentials = "true")
public class SecurityConfig {
//    @Autowired
//    MongoUserDetailsService mongoUserDetailsService;
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new MongoUserDetailsService();
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .formLogin() ;// 1. Use Spring Login Form
        http.cors()
                .and()
                //  csrf
//                 .csrf()
//                 .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable() // Note: for demonstration purposes only, this should not be done
//                .and()
                .authorizeRequests() // 2. Require authentication in all endpoints except login
//                .antMatchers(HttpMethod.POST,"/delivery").hasAuthority("DISPATCHER")   // OK
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
//                .httpBasic() // 3. Use Basic Authentication
//                .and()
                .sessionManagement().disable();
        return http.build();
    }



//    @Bean
//    AuthenticationManager myAuthenticationManager(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider::authenticate;
//    }
//    @Bean
//// Define an instance of Bcrypt for hashing passwords
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
