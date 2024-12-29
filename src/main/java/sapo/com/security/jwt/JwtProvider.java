package sapo.com.security.jwt;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sapo.com.security.user_principal.UserPrincipal;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${expired}")
    private Long EXPIRED ;

    @Value("${secret_key}")
    private String SECRET_KEY;
    private Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
    public String generateToken(UserPrincipal userPrincipal ){

        return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+EXPIRED) )
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY).compact();
    }
    public Boolean validate(String token){

        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true ;
        }catch (ExpiredJwtException expiredJwtException){
            logger.error("Expired Token {}", expiredJwtException.getMessage());
        }catch (SignatureException signatureException){
            logger.error("Invalid Signature Token {}", signatureException.getMessage());
        }catch (MalformedJwtException malformedJwtException){
            logger.error("Invalid format {}", malformedJwtException.getMessage());
        }catch (UnsupportedJwtException unsupportedJwtException){
            logger.error("UnSupported token {}",unsupportedJwtException.getMessage());
        }
        return false ;

    }

    public String getUserNameFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
