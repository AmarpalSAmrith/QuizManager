package com.synoptic.project.quiz.manager.configuration;

import com.synoptic.project.quiz.manager.handler.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String ROLE_EDIT = "EDIT";
  private static final String ROLE_VIEW = "VIEW";
  private static final String ROLE_RESTRICTED = "RESTRICTED";


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("editUser")
        .password(getPasswordEncoder().encode("password"))
        .roles(ROLE_EDIT);
    auth.inMemoryAuthentication()
        .withUser("viewUser")
        .password(getPasswordEncoder().encode("password"))
        .roles(ROLE_VIEW);
    auth.inMemoryAuthentication()
        .withUser("restrictedUser")
        .password(getPasswordEncoder().encode("password"))
        .roles(ROLE_RESTRICTED);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    String[] restrictedOnlyUrls = {"/quiz/", "/**/home/**", "/**/view/**"};
    String[] viewOnlyUrls = {"/question/**"};
    String[] editOnlyUrls = {"/**/edit/**", "/**/new/**", "/**/update/**", "/**/delete/**", "/**/add"};

    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/", "/login").permitAll()
        .antMatchers(editOnlyUrls).hasRole(ROLE_EDIT)
        .antMatchers(viewOnlyUrls).hasAnyRole(ROLE_VIEW, ROLE_EDIT)
        .antMatchers(restrictedOnlyUrls).hasAnyRole(ROLE_RESTRICTED, ROLE_VIEW, ROLE_EDIT)
        .and().formLogin()
        .and().logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .and()
        .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler())
    ;
  }


  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

}