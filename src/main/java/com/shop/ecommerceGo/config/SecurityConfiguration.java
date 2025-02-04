package com.shop.ecommerceGo.config;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
            "/client/**",
            "/css/**",
            "/js/**",
            "/images/**"
          )
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .formLogin(formLogin ->
        formLogin.loginPage("/login").failureUrl("/login?error").permitAll()
      );

    return http.build();
  }
}
