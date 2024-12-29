package sapo.com.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider ;

    @Autowired
    private UserDetailsService userDetailsService ;
    private final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = getTokenFromRequest(request);
            if(token != null && jwtProvider.validate(token)){
                String email = jwtProvider.getUserNameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email );
                if(userDetails !=null ){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null , userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
            }

        }catch (Exception exception){
            logger.error("Un authentication ... {}", exception.getMessage());
        }
        filterChain.doFilter(request,response);
    }
    public  String getTokenFromRequest (HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if (header!= null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null ;
    }
}
