package me.shinsunyoung.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailService userService;





    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**", "/img/**");
    }


    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/login", "/join", "/quiz/bQuiz", "/games/blocks", "/user", "/quiz/submitAnswers",
                                "/policyInfor/policies", "/login/*",  "/policy/*",  "/coramdeobiblequiz", "/main/home?query=${date}",
                                 "/board/list", "/board/view", "/login/resetpassword", "/church/churchUsers", "/church", "/church/churches",
                                 "/login/successresetpassword", "users/*", "/users/{userId}", "/notice/customerCenter_notice", "/faqUserView",
                                 "/users/rankings", "/api/check-email", "/api/check-nickname", "/api/check-phone").permitAll()
                               .requestMatchers("/board/write", "/board/writepro", "/board/delete",
                                "/board/modify/{boardId}", "/board/update/{boardId}",
                                "/users/modify", "/users/view", "/users/update",  "/admin/IdUserFromCart").hasRole("USER")
                        .requestMatchers("/*", "/*/*", "/admin/*", "/admin/AllUsers", "/quiz/createQuizQuestion", "/quiz/listQuizQuestions",
                                "/admin/IdUser", "/admin/userDelete", "/notice/upload", "/notice/adminnoticeUpload", "/notice/edit/{noticeId}", "/notice/list",
                                "/notice/delete/{noticeId}", "/notice/update", "/adminboard/view",
                                "/faqList", "/faqAdd", "faq/edit/{id}", "faq/{id}", "/faqUpoad", "faq/delete/{id}", "/faqUserView",
                        "/respondForm", "/respond", "/responds/view/{id}", "/responds/edit/{id}", "/responds/edit", "/responds/delete/{id}",
                                "/admin/events", "/admin/events/create", "/admin/events/delete/{id}",
                                "/admin/change",   "/responds/edit/{id}/{boardId}", "/responds/update").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new CustomSuccessHandler()))
                .logout(logout -> logout
                        .logoutSuccessUrl("/coramdeobiblequiz")
                        .invalidateHttpSession(true))
                .csrf().disable();

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }


}
