package edu.tum.ase.casservice.jwt;


import edu.tum.ase.casservice.model.AseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//generate and verify JWT tokens
@Component
public class JwtUtil {
    @Autowired
    private KeyStoreManager keyStoreManager;

    public String generateToken(AseUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    // Create JWS with both custom and registered claims, signed by a private key.
    private String createToken(Map<String, Object> claims, String subject) {
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("aseproject")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5)) // Expires after 5h

                .signWith(keyStoreManager.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
        return jwt;
    }
    // Create a Parser to read info inside a JWT. This parser use the public key
    // to verify the signature of incoming JWT tokens
    private JwtParser loadJwtParser() {
        // signing key:verify whether a JWS is signed by a trusted, intended party
        return Jwts.parserBuilder()
                .setSigningKey(keyStoreManager.getPublicKey())
                .build();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return loadJwtParser()
                .parseClaimsJws(token)
                .getBody();
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Check if the JWT is signed by us, and is not expired
    public boolean verifyJwtSignature(String token) {
        // TODO: Check if the JWT is signed by the public key in the KeyStore
        // and the JWT is not expired
        return (!isTokenExpired(token) && loadJwtParser().isSigned(token));

    }
}