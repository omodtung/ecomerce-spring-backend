package com.shop.ecommerceGo.config;

import com.shop.ecommerceGo.config.CustomSuccessHandler;
import com.shop.ecommerceGo.service.CustomUserDetailsService;
import com.shop.ecommerceGo.service.UserService;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpMessageConverterAuthenticationSuccessHandler.AuthenticationSuccess;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService(UserService userService) {
    return new CustomUserDetailsService(userService);
  }

  // Dieu huong ben trong trang login cua spring
  //   @Bean
  //   public AuthenticationManager authenticationManager(
  //     HttpSecurity http,
  //     PasswordEncoder passwordEncoder,
  //     UserDetailsService userDetailsService
  //   ) throws Exception {
  //     AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
  //       AuthenticationManagerBuilder.class
  //     );
  //     authenticationManagerBuilder
  //       .userDetailsService(userDetailsService)
  //       .passwordEncoder(passwordEncoder);
  //     return authenticationManagerBuilder.build();
  //   }

  @Bean
  public DaoAuthenticationProvider authProvider(
    PasswordEncoder passwordEncoder,
    UserDetailsService userDetailsService
  ) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    // authProvider.setHideUserNotFoundExceptions(false);
    return authProvider;
  }

  @Bean
  public AuthenticationSuccessHandler customSuccessHandler() {
    return new CustomSuccessHandler();
  }

  @Bean
  public SpringSessionRememberMeServices rememberMeServices() {
    SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
    // optionally customize
    rememberMeServices.setAlwaysRemember(true);
    return rememberMeServices;
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // v6. lamda
    http
      .authorizeHttpRequests(authorize ->
        authorize
          .dispatcherTypeMatchers(
            DispatcherType.FORWARD,
            DispatcherType.INCLUDE
          )
          .permitAll()
          .requestMatchers(
            "/",
            "/login",
            "/product/**",
            "/client/**",
            "/css/**",
            "/js/**",
            "/images/**"
          )
          .permitAll()
          .requestMatchers("/admin/**")
          .hasRole("ADMIN")
          .anyRequest()
          .authenticated()
      )
      .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/logout?expired")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false))
      .logout(logout ->
        logout.deleteCookies("JSESSIONID").invalidateHttpSession(true)
      )
      .rememberMe(r -> r.rememberMeServices(rememberMeServices()))
      .formLogin(formLogin ->
        formLogin
          .loginPage("/login")
          .failureUrl("/login?error")
          .successHandler(customSuccessHandler())
          .permitAll()
      )
      .exceptionHandling(ex -> ex.accessDeniedPage("/access-deny"));
    return http.build();
  }
}
