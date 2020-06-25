package com.synoptic.project.quiz.manager.configuration;

import com.synoptic.project.quiz.manager.handler.CustomAccessDeniedHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String ROLE_EDIT = "EDIT";
  private static final String ROLE_VIEW = "VIEW";
  private static final String ROLE_RESTRICTED = "RESTRICTED";

  @Autowired
  UserDetailsService userDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  private String[] getAcceptedUrls(final String role) {
    // TODO: configure the allowed URLs
    List<String> restrictedOnlyUrls = new ArrayList<>(Arrays.asList("/**/view/**", "/**/home/**"));
    List<String> viewOnlyUrls = new ArrayList<>(
        Arrays.asList("/user/**", "/quiz/**"));
    List<String> editOnlyUrls = new ArrayList<>(
        Arrays.asList("/admin", "/**/edit/**", "/**/new/**", "/**/update/**", "/**/delete/**"));
    switch (role) {
      case ROLE_RESTRICTED:
        return restrictedOnlyUrls.toArray(new String[0]);
      case ROLE_VIEW:
        viewOnlyUrls.addAll(restrictedOnlyUrls);
        return viewOnlyUrls.toArray(new String[0]);
      case ROLE_EDIT:
        editOnlyUrls.addAll(viewOnlyUrls);
        editOnlyUrls.addAll(restrictedOnlyUrls);
        return editOnlyUrls.toArray(new String[0]);
    }
    throw new IllegalArgumentException(role + " is not a recognised role");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    String[] restrictedOnlyUrls = {"/**/view/**", "/**/home/**"};
    String[] viewOnlyUrls = {"/user/**", "/quiz/**"};
    String[] editOnlyUrls = {"/admin", "/**/edit/**", "/**/new/**", "/**/update/**", "/**/delete/**"};

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
    return NoOpPasswordEncoder.getInstance();
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }
}
