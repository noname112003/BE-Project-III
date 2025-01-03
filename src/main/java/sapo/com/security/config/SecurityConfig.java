package sapo.com.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sapo.com.security.jwt.AccessDenied;
import sapo.com.security.jwt.JwtEntryPoint;
import sapo.com.security.jwt.JwtProvider;
import sapo.com.security.jwt.JwtTokenFilter;
import sapo.com.security.user_principal.UserDetailService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailService userDetailService ;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Autowired
    private JwtTokenFilter jwtTokenFilter ;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AccessDenied accessDenied ;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(config -> config.configurationSource(request -> {
                    CorsConfiguration cf = new CorsConfiguration();
                    cf.setAllowedOrigins(List.of("http://localhost:5173","https://project-iii-b6d85.web.app/","https://project-iii-b6d85.firebaseapp.com/"));
                    cf.setAllowedMethods(List.of("*"));
                    cf.setAllowCredentials(true);
                    cf.setAllowedHeaders(List.of("*"));
                    cf.setExposedHeaders(List.of("*"));
                    return cf;
                }))
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(
                        (auth)->auth
//                                .requestMatchers("/v1/auth/**")
//                                .permitAll()
//                                .requestMatchers("/v1/user/**")
//                                .permitAll()
//                                .requestMatchers(HttpMethod.GET ,"/v1/admin/**","/v1/role/**" , "/v1/customers/**" , "/v1/products/**" , "/v1/orders/**" )
//                                .permitAll()
//                                .requestMatchers("/v1/admin/reset_password/**")
//                                .permitAll()
//
//                                .requestMatchers(HttpMethod.POST , "/v1/admin/**","/v1/user/**","/v1/role/**" )
//                                .hasAuthority("ROLE_ADMIN")
//                                .requestMatchers(HttpMethod.PUT , "/v1/admin/**","/v1/user/**","/v1/role/**"  )
//                                .hasAuthority("ROLE_ADMIN")
//                                .requestMatchers(HttpMethod.DELETE , "/v1/admin/**","/v1/user/**","/v1/role/**" )
//                                .hasAuthority("ROLE_ADMIN")


//                                .requestMatchers(HttpMethod.POST , "/v1/customers/**")
//                                .hasAuthority("ROLE_SUPPORT")
//                                .requestMatchers(HttpMethod.PUT , "/v1/customers/**")
//                                .hasAuthority("ROLE_SUPPORT")
//                                .requestMatchers(HttpMethod.DELETE , "/v1/customers/**")
//                                .hasAuthority("ROLE_SUPPORT")
//
//
//                                .requestMatchers(HttpMethod.POST , "/v1/products/**")
//                                .hasAuthority("ROLE_REPOSITORY")
//                                .requestMatchers(HttpMethod.PUT , "/v1/products/**")
//                                .hasAuthority("ROLE_REPOSITORY")
//                                .requestMatchers(HttpMethod.DELETE , "/v1/products/**")
//                                .hasAuthority("ROLE_REPOSITORY")
//
//
//                                .requestMatchers(HttpMethod.POST , "/v1/orders/**")
//                                .hasAuthority("ROLE_SALE")
//                                .requestMatchers(HttpMethod.PUT , "/v1/orders/**")
//                                .hasAuthority("ROLE_SALE")
//                                .requestMatchers(HttpMethod.DELETE , "/v1/orders/**")
//                                .hasAuthority("ROLE_SALE")


//                                .requestMatchers( HttpMethod.GET , "/v1/products" , "/v1/categories" )
//                                .permitAll()
//                                .requestMatchers(HttpMethod.POST , "/admin/products" , "/admin/categories")
//                                .authenticated("ROLE_ADMIN")
//                                .requestMatchers(HttpMethod.PUT , "/admin/products" , "/admin/categories/")
//                                .authenticated()
//                                .requestMatchers(HttpMethod.DELETE , "/admin/products" , "/admin/categories/")
//                                .authenticated()
//                                .requestMatchers("v1/admin/role/{id}")
//                                .authenticated()
                                .anyRequest().permitAll()

                                )


                .exceptionHandling(
                        (auth)->auth.authenticationEntryPoint(jwtEntryPoint)
                                .accessDeniedHandler(accessDenied))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //Make the below setting as * to allow connection from any hos
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST" ,"PUT" , "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
//        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
