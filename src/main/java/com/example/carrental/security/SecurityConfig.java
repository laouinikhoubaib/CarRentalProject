package com.example.carrental.security;

import com.example.carrental.Enumerations.Role;
import com.example.carrental.security.jwt.JwtAccessDenied;
import com.example.carrental.security.jwt.JwtAuthenticationHttp403;
import com.example.carrental.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private JwtAccessDenied jwtAccessDenied;
    
    @Autowired
    private JwtAuthenticationHttp403 jwtAuthenticationHttp403;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
        http.authorizeRequests()
                .antMatchers("/api/authentication/**", "/swagger-ui/","/swagger-ui/**",
                        "/v3/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security",
                        "/swagger-ui.html", "/webjars/**", "/forum/**", "/chat-websocket/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/api/admin/**").hasRole(Role.SUPERADMIN.name())
                .antMatchers("/api/complaint/**").hasRole(Role.SUPERADMIN.name())
                .antMatchers("/api/agence/**").permitAll()
                .antMatchers("/api/vehicule/**").permitAll()
                .antMatchers("/api/reservation/**").permitAll()
                .antMatchers("/api/user/**").permitAll()
                .anyRequest().authenticated()
		        .and()
		       // .oauth2Login();
		        // .and()
		         .exceptionHandling().accessDeniedHandler(jwtAccessDenied)
		         .authenticationEntryPoint(jwtAuthenticationHttp403);
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter()
    {
        return new JwtAuthorizationFilter();
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


    private static final String[] AUTH_WHITELIST = {
            "/**",
            "/v3/api-docs/**",
            "/api/**",
            "/swagger-ui/**",
            "/swagger-ui.html/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/**",
            "/api/user/**",
            "/api/auth/**",
            "/api/admin/**"
    };
    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedOrigins("/api/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")

                        .allowedMethods("*");
            }
        };
    }


}