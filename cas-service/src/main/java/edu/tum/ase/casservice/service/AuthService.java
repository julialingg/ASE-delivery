package edu.tum.ase.casservice.service;


import edu.tum.ase.casservice.jwt.JweUtil;
import edu.tum.ase.casservice.jwt.JwtUtil;
import edu.tum.ase.casservice.jwt.KeyStoreManager;
import edu.tum.ase.casservice.model.AuthRequest;
import edu.tum.ase.casservice.repository.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

//  authenticate users
@RestController
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;
    @Autowired
    private MongoUserDetailsService mongoUserDetailsService;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JweUtil jweUtil;
    @Autowired
    private KeyStoreManager keyStoreManager;
//    @Autowired
//    private CookieConfig cookieConfig;

    public ResponseEntity<String> authenticateUser(String authorization, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO: Get the username and password by decoding the Base64 credential inside the Basic Authentication
        // basic authentication: reading a username and password from the HttpServletRequest
        System.out.println("request");
        System.out.println(request.getCookies());
        System.out.println(request.getHeaderNames());
        System.out.println("authorization");
        System.out.println(authorization);
        final String basicAuthCredential = authorization.substring("Basic".length()).trim(); // 截basic后面的<username:password>
        //trim()去除string两边的空格
        System.out.println("Authorization is " + authorization);
        final byte[] credentialBytes = Base64.decodeBase64(basicAuthCredential);
        final String[] credential = new String(credentialBytes, StandardCharsets.UTF_8).split(":");
        final String username = credential[0];
        final String password = credential[1];
        // TODO: find if there is any user exists in the database based on the credential,

        // get user details
        final UserDetails details=mongoUserDetailsService.loadUserByUsername(username);
        // correct password compared with the database
        if (bcryptPasswordEncoder.matches(password,details.getPassword()))
        {
            // TODO: add authenticate the user using the Spring Authentication Manager
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(details, password, details.getAuthorities());
            Authentication auth = null;
            try{
                auth= authenticationManager.authenticate(token);
//                SecurityContextHolder.getContext().setAuthentication(auth);
                // TODO: Generate a JWT token based on the info of the authenticated user
                String jwt = null;
                jwt=jwtUtil.generateToken(userRepository.getAseUserByUsername(details.getUsername()));
                // Cookie
                Cookie jwtCookie = new Cookie("jwt", jwt); // cookie name and value
                // TODO: Configure the cookie to be HttpOnly and
                jwtCookie.setHttpOnly(true);
                //  expires after a period
                jwtCookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                jwtCookie.setSecure(false);

                //then include the cookie into the response
                response.addCookie(jwtCookie);
//              String str="authenticate OK ";

                return new ResponseEntity<String>(jwt, HttpStatus.OK);
//                return new ResponseEntity<String>(str, HttpStatus.OK);
            } catch (BadCredentialsException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Email or password is incorrect", HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        // incorrect Email or password
        else{

            return new ResponseEntity<String>("Password is incorrect",HttpStatus.BAD_REQUEST);
//            String str="Wrong password";
//            return new ResponseEntity(str,HttpStatus.valueOf(403));
        }
    }


    public void setAuthentication(User userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }


    //read the password from the frontend request
    public ResponseEntity<?> createAuthToken(AuthRequest request) throws Exception {
        String  pw= jweUtil.decryptPasswordInJwe(request.getPassword());

        final UserDetails aseUserDetails=mongoUserDetailsService.loadUserByUsername(request.getUsername());

        Authentication authentication=null;
        try{
            UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(aseUserDetails,pw);
            authentication=authenticationManager.authenticate(token);

        }catch (UsernameNotFoundException ex){
            ex.printStackTrace();
            return new ResponseEntity<String>("Email or password is incorrect",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("error in authenticating the user",HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(authentication !=null){
            final String jwt=jwtUtil.generateToken(userRepository.getAseUserByUsername(aseUserDetails.getUsername()));

            HashMap<String,String> jwtResponse =new HashMap<>();
            jwtResponse.put("jwt",jwt);
            return new ResponseEntity<HashMap<String,String>>(jwtResponse,HttpStatus.OK);
        }else {
            return new ResponseEntity<String >("Email or password is incorrect",HttpStatus.BAD_REQUEST);
        }
    }


    //  frontend need public key
    public HashMap<String, String> getPublicKeyData() throws Exception {
        HashMap<String, String> pKeyResponse = new HashMap<String, String>();
        RSAPublicKey rsaPubKey = (RSAPublicKey) keyStoreManager.getPublicKey();
        System.out.println("begin rsaPubKey");
        System.out.println(rsaPubKey);
        System.out.println("end rsaPubKey");

        // TODO: Get Modulus of Public key
        byte[] modulusByte = rsaPubKey.getModulus().toByteArray();
        System.out.println(modulusByte);

        // Format the modulus into byte format (e.g. AB:CD:E1)
        String modulusByteStr = "";
        for (byte b : modulusByte) {
            modulusByteStr += String.format("%02X:", b);
        }

        modulusByteStr = modulusByteStr.substring(0, modulusByteStr.length() - 1);
        System.out.println(modulusByteStr);

        // TODO: Get encoded public key)
//        byte[] a=rsaPubKey.getEncoded();
//
        pKeyResponse.put("key",rsaPubKey.getEncoded().toString());
        pKeyResponse.put("n", modulusByteStr);
        // TODO: Get public exponent)
        pKeyResponse.put("e", rsaPubKey.getPublicExponent().toString());
        return pKeyResponse;
    }
}
