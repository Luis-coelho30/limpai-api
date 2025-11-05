package br.com.limpai.projeto_limpai.service;

import br.com.limpai.projeto_limpai.exception.user.CredenciaisIncorretasException;
import br.com.limpai.projeto_limpai.exception.user.JwtParsingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${limpai.jwt.secret}")
    private String secretKey;

    private String generateToken(Long id, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id.toString());
        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long extractUserId(String token) {
        String idString = extractAllClaims(token).get("id", String.class);
        try {
            return Long.parseLong(idString);
        } catch (NumberFormatException ex) {
            throw new JwtParsingException("Formato do ID inválido.");
        }
    }

    public String extractUserRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public String extractUserMail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.JwtException e) {
            throw new CredenciaisIncorretasException("Token inválido ou expirado.");
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userMail = extractUserMail(token);
        return userMail != null &&
                userMail.equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
    }
}
