package com.rect2m.config;

import com.rect2m.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register").permitAll()  // Доступ до реєстрації
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/menu").permitAll() // Доступ до входу
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .anyRequest().authenticated()  // Інші запити потребують авторизації
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")  // Ваша сторінка входу
                        .defaultSuccessUrl("/menu",
                                true)  // Перенаправлення на головну сторінку після успішного входу
                        .failureUrl("/auth/login?error=true")  // Перенаправлення при помилці входу
                        .permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/auth/register")  // Ваша сторінка входу
                        .defaultSuccessUrl("/menu",
                                true)  // Перенаправлення на головну сторінку після успішного входу
                        .failureUrl(
                                "/auth/register?error=true")  // Перенаправлення при помилці входу
                        .permitAll()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl(
                                        "/menu") // куди перенаправити після успішного входу
                                .failureUrl("/login?error") // куди перенаправити у разі помилки
                )
                .logout(logout ->
                                logout
                                        .logoutSuccessUrl(
                                                "/menu") // URL, на який перенаправить після виходу
                                        .invalidateHttpSession(true) // видалення сесії
                                        .clearAuthentication(true) // очистка автентифікації
                                        .deleteCookies("JSESSIONID")
                        // видалення cookie з ідентифікатором сесії
                );

        return http.build();
    }
}
