package com.example.production.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String FETCH_USERS_QUERY = """
            select email, password, enabled
            from employees
            where email = ?;
            """;

    private static final String FETCH_AUTHORITIES_QUERY = """
            select user_email, authority
             from roles r,
                  authorities a
             where user_email = ?
             and r.authority_id = a.id;
            """;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    private final DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(FETCH_USERS_QUERY)
                .authoritiesByUsernameQuery(FETCH_AUTHORITIES_QUERY)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/employees/all")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/profile/edit")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/profile/resumes/**")).hasAnyAuthority("APPLICANT")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/profile/vacancies/**")).hasAnyAuthority("EMPLOYER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/profile/listVacancies")).hasAnyAuthority("APPLICANT")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/profile/listVacancies/**")).hasAnyAuthority("APPLICANT")
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
