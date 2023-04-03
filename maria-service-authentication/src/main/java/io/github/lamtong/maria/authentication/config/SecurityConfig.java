/*
Copyright 2023 the original author, Lam Tong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package io.github.lamtong.maria.authentication.config;

import io.github.lamtong.maria.authentication.context.KaptchaContext;
import io.github.lamtong.maria.authentication.core.AccessDeniedHandlerImpl;
import io.github.lamtong.maria.authentication.core.JwtUsernamePasswordAuthenticationFilter;
import io.github.lamtong.maria.authentication.core.LogoutSuccessHandlerImpl;
import io.github.lamtong.maria.authentication.filter.JwtAuthorizedRequestFilter;
import io.github.lamtong.maria.constant.SecurityConstant;
import io.github.lamtong.maria.constant.ServiceAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * {@code Maria-Web} 认证授权中心安全配置类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"deprecation", "SpringJavaAutowiredFieldsWarningInspection"})
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private KaptchaContext kaptchaContext;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter() throws Exception {
        JwtUsernamePasswordAuthenticationFilter filter = new JwtUsernamePasswordAuthenticationFilter(this.kaptchaContext, this.redisTemplate, this.kafkaTemplate);
        filter.setAuthenticationManager(super.authenticationManager());
        filter.setFilterProcessesUrl(SecurityConstant.AUTH_LOGIN);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(ServiceAuthentication.URL_LOGOUT_NOTICE_VALUE)
                .antMatchers(SecurityConstant.AUTH_KAPTCHA)
                .antMatchers(SecurityConstant.SWAGGER_UI_HTML)
                .antMatchers(SecurityConstant.SWAGGER_UI)
                .antMatchers(SecurityConstant.SWAGGER_RESOURCES)
                .antMatchers(SecurityConstant.V2)
                .antMatchers(SecurityConstant.WEBJARS)
                .antMatchers(SecurityConstant.DRUID)
                .antMatchers(SecurityConstant.ACTUATOR);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable().authorizeRequests()
                .antMatchers(SecurityConstant.AUTH_LOGIN).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .logout()
                .logoutUrl(SecurityConstant.AUTH_LOGOUT)
                .logoutSuccessHandler(new LogoutSuccessHandlerImpl(this.redisTemplate))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .and()
                .addFilterAt(jwtUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizedRequestFilter(this.redisTemplate), JwtUsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
