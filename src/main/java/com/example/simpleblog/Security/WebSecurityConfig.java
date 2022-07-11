package com.example.simpleblog.Security;


import com.example.simpleblog.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        super.setApplicationContext(context);
        AuthenticationManagerBuilder globalAuthBuilder = context.getBean(AuthenticationManagerBuilder.class);
        try {
            globalAuthBuilder.userDetailsService(userService);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder.bCryptPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationSuccessHandler appAuthenticationSuccessHandler(){
        return new AppAuthenticationSuccessHandler();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers().permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();
        http.cors().and().csrf().disable();
    }
}
