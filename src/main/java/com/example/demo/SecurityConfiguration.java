package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


//the two annotations tell the compiler that the file is a configuration
// file and Spring Security is enabled for the application
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends
        WebSecurityConfigurerAdapter {

    //creates an object that can be re-used to encode passwords in the application.
    @Bean
    //this creates an object that can be re-used to encode passwords in the application.
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                //tells the application which requests should be authorized
                //in this example, any request that is authenticated should
                // be permitted.
                .authorizeRequests()
                .antMatchers("/")
                .access("hasAnyAuthority('USER', 'ADMIN')")
                .antMatchers("/admin").access("hasAuthority('ADMIN')")
                .anyRequest().authenticated()
                //adds additional authentication rules.
                .and()
                //this indicates that the application should show a login form.
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(
                        new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll();
    }
    @Override
    //this overrides the default configure method, configures users who can
    // access the application. by default, Spring Boot will provide a new
    // random password assigned to the user "user" when it starts up, if
    // you do not include this method.
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception{
            auth.inMemoryAuthentication()
            .withUser("dave").password(passwordEncoder().encode("begreat")).authorities("ADMIN")
                .and()
                .withUser("user").password(passwordEncoder().encode("password")).authorities("USER");
    }
}