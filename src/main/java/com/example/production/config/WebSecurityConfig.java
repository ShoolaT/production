package com.example.production.config;

import com.example.production.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/home")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/home")
                )
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/credits/create")).hasAnyAuthority("ACCOUNTANT", "ADMIN")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/credits/all")).hasAnyAuthority("ACCOUNTANT", "ADMIN", "DIRECTOR")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/credits/pay/**")).hasAnyAuthority("ACCOUNTANT", "ADMIN")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/credits/**/history")).hasAnyAuthority("ACCOUNTANT", "ADMIN", "DIRECTOR")

                                .requestMatchers(AntPathRequestMatcher.antMatcher("/employees/create")).hasAnyAuthority("ADMIN")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/employees/all")).hasAnyAuthority("ADMIN", "DIRECTOR")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/employees/{id}")).hasAnyAuthority("ADMIN", "DIRECTOR")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/employees/{id}/**")).hasAnyAuthority("ADMIN")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/products/create")).hasAnyAuthority("ADMIN", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/products/all")).hasAnyAuthority("ADMIN", "DIRECTOR", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/products/{id}/edit")).hasAnyAuthority("ADMIN", "MANAGER")
//                        .requestMatchers(AntPathRequestMatcher.antMatcher("/products/{id}/delete/confirmed")).hasAnyAuthority("ADMIN", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/materials/create")).hasAnyAuthority("ADMIN", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/materials/all")).hasAnyAuthority("ADMIN", "DIRECTOR", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/materials/{id}/edit")).hasAnyAuthority("ADMIN", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/materials/{id}/delete/confirmed")).hasAnyAuthority("ADMIN", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/materialPurchases/create")).hasAnyAuthority("ADMIN", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/materialPurchases/all")).hasAnyAuthority("ADMIN", "DIRECTOR", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/materialPurchases/edit/{id}")).hasAnyAuthority("ADMIN")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/materialPurchases/{id}/delete/confirmed")).hasAnyAuthority("ADMIN")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/productSales/create")).hasAnyAuthority("ADMIN", "MANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/productSales/all")).hasAnyAuthority("ADMIN", "DIRECTOR", "MANAGER")

                                .requestMatchers(AntPathRequestMatcher.antMatcher("/ingredients/create")).hasAnyAuthority("ADMIN", "TECHNOLOGIST")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/ingredients/all")).hasAnyAuthority("ADMIN", "DIRECTOR", "TECHNOLOGIST")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/ingredients/{id}/edit")).hasAnyAuthority("ADMIN", "TECHNOLOGIST")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/ingredients/{id}/delete/confirmed")).hasAnyAuthority("ADMIN", "TECHNOLOGIST")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/productProductions/create")).hasAnyAuthority("ADMIN", "TECHNOLOGIST")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/productProductions/all")).hasAnyAuthority("ADMIN", "DIRECTOR", "TECHNOLOGIST")

                                .requestMatchers(AntPathRequestMatcher.antMatcher("/salaries/all")).hasAnyAuthority("ADMIN", "DIRECTOR", "ACCOUNTANT")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/salaries/{id}/edit")).hasAnyAuthority("ADMIN", "ACCOUNTANT")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/salaries/issue")).hasAnyAuthority("ADMIN", "ACCOUNTANT")
                                .anyRequest().permitAll()
                );
        return http.build();
    }
}