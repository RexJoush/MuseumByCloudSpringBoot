package com.nwu.config;

import com.nwu.filter.AuthenticationTokenFilter;
import com.nwu.util.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author Rex Joush
 * @time 2021.03.20
 */
@Configuration      // 声明为配置类
@EnableWebSecurity      // 启用 Spring Security web 安全的功能
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 配置 spring security 的 UserDetails 实现类
    @Resource
    private UserDetailsService userDetailsService;

//    @Bean
//    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
//        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
//        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
//        return authenticationTokenFilter;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
//                .antMatchers("/customize/index").permitAll()
//                .antMatchers("/customize/home").hasAuthority("USER")
//                .antMatchers("/customize/level1/*").hasAuthority("ADMIN")
//                .antMatchers("/user/*").permitAll()
                .anyRequest().authenticated();       // 需携带有效 token



////                .antMatchers("/admin").hasAuthority("admin")   // 需拥有 admin 这个权限
////                .antMatchers("/ADMIN").hasRole("ADMIN")     // 需拥有 ADMIN 这个身份
//                .anyRequest().permitAll()       // 允许所有请求通过
//                .and()
//                .csrf()
//                .disable()                      // 禁用 Spring Security 自带的跨域处理
//                .sessionManagement()                        // 定制我们自己的 session 策略
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 调整为让 Spring Security 不创建和使用 session


        /**
         * 本次 json web token 权限控制的核心配置部分
         * 在 Spring Security 开始判断本次会话是否有权限时的前一瞬间
         * 通过添加过滤器将 token 解析，将用户所有的权限写入本次 Spring Security 的会话
         */
//         http
//                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(password());
        auth.userDetailsService(myUserService());
    }

    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }

    //@Bean
    public UserDetailsService myUserService(){
        return new MyUserDetailsService();
    }
}
