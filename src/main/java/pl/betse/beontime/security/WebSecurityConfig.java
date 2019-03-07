package pl.betse.beontime.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ROOT_ENDPOINT = "/**";
    private static final String LOGIN_ENDPOINT = "/login";

    private final JwtService jwtService;
    private final CustomAuthenticationProvider authProvider;

    @Autowired
    public WebSecurityConfig(JwtService jwtService, CustomAuthenticationProvider authProvider) {
        this.jwtService = jwtService;
        this.authProvider = authProvider;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, ROOT_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.POST, LOGIN_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new UserAndPasswordLoginFilter(LOGIN_ENDPOINT, authenticationManager(), jwtService),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }
}
